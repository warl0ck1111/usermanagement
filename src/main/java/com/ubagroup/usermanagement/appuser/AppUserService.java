package com.ubagroup.usermanagement.appuser;


import com.ubagroup.usermanagement.exception.ApiRequestException;
import com.ubagroup.usermanagement.account.otp.Otp;
import com.ubagroup.usermanagement.account.otp.OtpService;
import com.ubagroup.usermanagement.role.Role;
import com.ubagroup.usermanagement.role.RoleNotFoundException;
import com.ubagroup.usermanagement.role.RoleRepository;
import com.ubagroup.usermanagement.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Okala III
 */
@Slf4j
@Service
public class AppUserService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MSG = "user with email %s not found";
    private static final int OTP_LENGTH = 6;
    private static final String OTP_MSG_TEMPLATE = "Your OTP is: %s";
    private static final int OTP_LIFETIME = 15;


    @Autowired
    private OtpService otpService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> {
            log.error(String.format(USER_NOT_FOUND_MSG, email));
            return new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email));
        });
    }


    /**
     *
     * @param appUser
     * @return Signup for external user (merchant)
     */
    @Transactional
    public String signUpExternalUser(AppUser appUser) {

        boolean userExists = appUserRepository.existsByEmail(appUser.getEmail());

        final String otp = otpService.generateOTP(OTP_LENGTH);
        log.info("YOUr otp is :" + otp);
        UserDetails userDetails = loadUserByUsername(appUser.getEmail());
        log.info("i'm herer signUpExternalUser");
        if (userExists) {
            if ( !userDetails.isEnabled()) { //if user not enabled that means its user first time
                sendOtpSms(appUser.getPhoneNo(),otp);
                log.info("OTP sent successfully");
                return "OTP sent successfully";
            }else {
                log.error("Email already exists");
                throw new ApiRequestException("Email already exists");
            }

        }
        String encodedPassword = bCryptPasswordEncoder.encode((appUser.getPassword()));
        appUser.setPassword(encodedPassword);
        appUser.setAppUserRole(appUser.getAppUserRole());
        AppUser externalUser = appUserRepository.save(appUser);

        Otp confirmationOTP = new Otp(otp, LocalDateTime.now(), LocalDateTime.now().plusMinutes(OTP_LIFETIME), externalUser);

        otpService.saveConfirmationToken(confirmationOTP);

        sendOtpSms(appUser.getPhoneNo(), otp);
        log.info(String.format(OTP_MSG_TEMPLATE, otp));

        return "OTP sent successfully";
    }


    /**
     *
     * @param appUser
     * @return sign up for internal user (merchant)
     */
    @Transactional
    public AppUser onboardLoginInternalUser(AppUser appUser, boolean isRM) {

        boolean userExists = appUserRepository.existsByEmail(appUser.getEmail());

        if (userExists) {
            AppUser internalUser = appUserRepository.findByEmail(appUser.getEmail()).get();
            if (isRM){
                if (!internalUser.getAppUserRole().getName().equalsIgnoreCase("RM")){
                    Role role = roleRepository.findByName("RM").get();
                    internalUser.setAppUserRole(role);
                    internalUser = appUserRepository.save(internalUser);
                }
            }
            return internalUser;
        }

        //on-board internal user

        if (isRM){
            Role role = roleRepository.findByName("RM").orElseThrow(()-> {
                log.error("RM role not does not exist");
                return new RoleNotFoundException("RM role not does not exist");
            });
            appUser.setAppUserRole(role);
        }
        Role role = roleRepository.findByName("VIEWER").orElseThrow(()-> {
            log.error("VIEWER role does not exist");
            return new RoleNotFoundException("VIEWER role does not exist");
        });

        appUser.setAppUserRole(role);
        String encodedPassword = bCryptPasswordEncoder.encode((appUser.getEmail()));
        appUser.setPassword(encodedPassword);
        appUser.setAppUserRole(appUser.getAppUserRole());
        appUser.setEnabled(true);
        AppUser internalUser = appUserRepository.save(appUser);

        return internalUser;
    }


    public void enableAppUser(String email) {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> {
            log.error("email does not exist");
            return new ApiRequestException("email does not exist");
        });
        appUser.setEnabled(true);
        appUserRepository.save(appUser);

    }


    private void sendOtpSms(String phoneNumber, String otp){
        smsService.send(phoneNumber, String.format(OTP_MSG_TEMPLATE,otp));
    }

}
