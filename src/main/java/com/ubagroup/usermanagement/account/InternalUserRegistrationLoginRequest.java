package com.ubagroup.usermanagement.account;

import lombok.Data;

/**
 * @author Okala III
 */

@Data
public class InternalUserRegistrationLoginRequest {
   private final String finacleId;
    private final String email;
    private final boolean  rmStatus;

}
