package com.ubagroup.usermanagement.account;

import lombok.Data;

@Data
public class ExternalUserLoginRequest {
    private String email;
    private String password;
}
