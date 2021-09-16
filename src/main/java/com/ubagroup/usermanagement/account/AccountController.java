package com.ubagroup.usermanagement.account;

import com.ubagroup.usermanagement.account.otp.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Okala III
 */

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/auth/")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OtpService otpService;


    @PostMapping(path = "external-user/signup")
    public ResponseEntity<String> registrationExternalUser(@RequestBody ExternalUserRegistrationRequest request) {
        String response = accountService.registerExternalUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "external-user/confirm-otp/{token}")
    public ResponseEntity<AuthenticationResponse> confirm(@PathVariable("token") String token) {
        AuthenticationResponse response = otpService.confirmOTP(token, accountService);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "external-user/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody ExternalUserLoginRequest request) throws Exception {
        AuthenticationResponse response = accountService.loginExternalUser(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping(path = "internal-user/login")
    public ResponseEntity<AuthenticationResponse> registrationInternalUser(@RequestBody InternalUserRegistrationLoginRequest request) {
        AuthenticationResponse response = accountService.loginInternalUser(request);
        return ResponseEntity.ok(response);
    }
}
