package com.ubagroup.usermanagement.account;

import lombok.Data;

/**
 * @author Okala III
 */

@Data
public class ExternalUserRegistrationRequest {
    private final String firstName;
    private final String lastName;
    private final String merchantId;
    private final String accountNo;
    private final String email;
    private final String password;
    private final Long  appUserRoleId;

}
