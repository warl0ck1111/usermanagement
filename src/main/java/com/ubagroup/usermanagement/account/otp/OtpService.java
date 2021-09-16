package com.ubagroup.usermanagement.account.otp;

import com.ubagroup.usermanagement.appuser.AppUser;
import com.ubagroup.usermanagement.appuser.AppUserRepository;
import com.ubagroup.usermanagement.appuser.AppUserService;
import com.ubagroup.usermanagement.exception.ApiRequestException;
import com.ubagroup.usermanagement.jwt.JwtUtil;
import com.ubagroup.usermanagement.account.AuthenticationResponse;
import com.ubagroup.usermanagement.account.AccountService;
import com.ubagroup.usermanagement.validator.EmailValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Okala III
 */
@Slf4j
@Data
@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;


    public void saveConfirmationToken(Otp token) {
        otpRepository.save(token);
    }

    public Optional<Otp> getOtp(String token) {
        return otpRepository.findByOtp(token);
    }

    public void setConfirmedAt(String token) {
        Otp otp = otpRepository.findByOtp(token).orElseThrow(() -> {
            log.error("invalid token");
            return new ApiRequestException("invalid token");
        });
        otp.setConfirmedAt(LocalDateTime.now());
        otpRepository.save(otp);
    }

    @Transactional
    public AuthenticationResponse confirmOTP(String token, AccountService accountService) {
        Otp otp = getOtp(token).orElseThrow(() -> {
            log.error("token not found");
            return new ApiRequestException("token not found");
        });
        if (otp.getConfirmedAt() != null) {
            log.error("user already confirmed");
            throw new ApiRequestException("user already confirmed");
        }

        LocalDateTime expiredAt = otp.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            log.error("token expired");
            throw new ApiRequestException(("token expired"));
        }

        setConfirmedAt(token);
        appUserService.enableAppUser(otp.getAppUser().getEmail());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(otp.getAppUser().getEmail());
        AppUser externalAppUser = appUserRepository.findByEmail(otp.getAppUser().getEmail()).get();

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwt, externalAppUser.getAppUserRole().getName(), externalAppUser.getId().toString());
    }

    public String generateOTP(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }
}
