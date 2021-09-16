package com.ubagroup.usermanagement.sms;

/**
 * @author Okala III
 */

public interface SmsSender {
    void send(String to, String content);
}
