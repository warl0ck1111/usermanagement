package com.ubagroup.usermanagement.sms;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Okala III
 */

@Data
@NoArgsConstructor
public class SmsRequest {
    private String channel;
    private String message;
    private List<Phone> to;

    public SmsRequest(String channel, String message, List<Phone> to) {
        this.channel = channel;
        this.message = message;
        this.to = to;
    }
}

@Data
@NoArgsConstructor
class Phone{
    private String phone;
}
