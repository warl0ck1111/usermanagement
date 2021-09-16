package com.ubagroup.usermanagement.account;

import com.ubagroup.usermanagement.appuser.AppUser;
import com.ubagroup.usermanagement.appuser.AppUserRepository;
import com.ubagroup.usermanagement.appuser.AppUserService;
import com.ubagroup.usermanagement.exception.ApiRequestException;
import com.ubagroup.usermanagement.jwt.JwtUtil;
import com.ubagroup.usermanagement.account.otp.OtpService;
import com.ubagroup.usermanagement.role.Role;
import com.ubagroup.usermanagement.role.RoleRepository;
import com.ubagroup.usermanagement.validator.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Okala III
 */

@Slf4j
@Service
public class AccountService {

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Transactional
    public String registerExternalUser(ExternalUserRegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            log.error("email not valid");
            throw new ApiRequestException("email not valid");
        }

        Role role = roleRepository.findByName("POS_OPS").orElseThrow(() -> {
            log.error("role does not exist");
            return new ApiRequestException("role does not exist");
        });

        return appUserService.signUpExternalUser(
                new AppUser(request.getFirstName(), request.getLastName(), request.getMerchantId(), request.getEmail(),
                        request.getPassword(), role));
    }

    @Transactional
    public AuthenticationResponse loginExternalUser(ExternalUserLoginRequest request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        } catch (BadCredentialsException ex) {
            log.error("invalid username or password");
            throw new ApiRequestException("invalid username or password", ex);
        }
        catch (DisabledException ex){
            log.error("user is disabled");
            throw new ApiRequestException("user is disabled");
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).get();

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponse(jwt, "MERCHANT", appUser.getId().toString());

    }


    @Transactional
    public AuthenticationResponse loginInternalUser(InternalUserRegistrationLoginRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            log.error("email not valid");
            throw new ApiRequestException("email not valid");
        }

        AppUser internalUser = appUserService.onboardLoginInternalUser(
                new AppUser(request.getFinacleId(), request.getEmail().trim()), request.isRmStatus());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(internalUser.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt, internalUser.getAppUserRole().getName(), internalUser.getId().toString());
    }


}
