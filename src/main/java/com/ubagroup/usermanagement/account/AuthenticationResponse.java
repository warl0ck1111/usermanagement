package com.ubagroup.usermanagement.account;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Okala III
 */

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwtToken;
    private String role;
    private String userId;


}
