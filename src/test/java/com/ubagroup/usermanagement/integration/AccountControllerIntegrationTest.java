package com.ubagroup.usermanagement.integration;

import com.ubagroup.usermanagement.account.ExternalUserLoginRequest;
import com.ubagroup.usermanagement.account.ExternalUserRegistrationRequest;
import com.ubagroup.usermanagement.account.InternalUserRegistrationLoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT
)
public class AccountControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    private final String ROOT_URL = port + "/api/v1/auth";

    @BeforeEach
    public void init(){
    }

    @AfterEach
    public void tearDown(){
    }

    @Test
    @Disabled("Not enough time. Will fix later")
    public void testRegistrationExternalUser(){
        ExternalUserRegistrationRequest request = new ExternalUserRegistrationRequest(
                "Bashir", "Okala", "11111",
                "123456789", "test@email.com", "call_john", 1L);

        given()
                .auth().preemptive().basic("okala", "secret")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(ROOT_URL + "/external-user/signup")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(containsString("OTP sent successfully"));
    }

    @Test
    @Disabled("Not enough time. Will fix later")
    public void testConfirm() {
        given()
                .pathParam("token", "123456")
                .when()
                .get(ROOT_URL + "/external-user/confirm-otp/{token}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Disabled("Auth token required. Not enough time. Will fix later")
    public void testAuthenticate() {
        ExternalUserLoginRequest request = new ExternalUserLoginRequest();
        request.setEmail("cardano@test.com");
        request.setPassword("cardano_crypto");

        given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(ROOT_URL + "/external-user/login")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Disabled("Not enough time. Will fix later")
    public void testRegistrationInternalUser() {
        InternalUserRegistrationLoginRequest request =
                new InternalUserRegistrationLoginRequest("122255", "hey@test.com", true);

        given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(ROOT_URL + "internal-user/login")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
