package com.ubagroup.usermanagement.sms;

/**
 * @author Okala III
 */

public class SmsNotSentException extends RuntimeException {

    public SmsNotSentException(String message) {
        super(message);
    }



}
