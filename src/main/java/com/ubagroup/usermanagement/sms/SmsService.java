package com.ubagroup.usermanagement.sms;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Okala III
 */
@Slf4j
@Service
public class SmsService implements SmsSender{
    private static final String SEND_SMS_URL = "http://VM-SC-NOTI:8030/send";

    @Override
    public void send(String to, String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setChannel("SMS");
        smsRequest.setMessage(content);

        List<Phone> phoneList = new ArrayList<>();
        smsRequest.setTo(phoneList);

        HttpEntity<SmsRequest> request = new HttpEntity<>(smsRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> jsonResponse = restTemplate.postForEntity(SEND_SMS_URL, request, String.class);
        Gson gson = new Gson();
        SmsResponse smsResponse = gson.fromJson(jsonResponse.toString(), SmsResponse.class);

        if (smsResponse.getCode() != 0) {
            log.error(String.format("There was a problem sending Sms to %s", to));
            throw new SmsNotSentException(String.format("There was a problem sending Sms to %s", to));
        }
    }

}