package com.ubagroup.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handlerApiRequestException(ApiRequestException ex){
        ApiException apiException = new ApiException(ex.getMessage(), ex, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handlerBadCredentialsException(ApiRequestException ex){
        ApiException apiException = new ApiException(ex.getMessage(), ex, HttpStatus.FORBIDDEN, LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(value = {DisabledException.class})
    public ResponseEntity<Object> handlerDisabledException(ApiRequestException ex){
        ApiException apiException = new ApiException(ex.getMessage(), ex, HttpStatus.FORBIDDEN, LocalDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);

    }
}
