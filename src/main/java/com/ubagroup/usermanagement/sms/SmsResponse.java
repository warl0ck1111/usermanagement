package com.ubagroup.usermanagement.sms;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Okala III
 */


@Data
@NoArgsConstructor
public class SmsResponse {
    private int code;
    private Result result;
}

class Result{
    private String id;
    private String channel;
    private String message;
    private List<Phone> to;
    private String status;
    private String createdOn;
    private String lastModified;

}