package com.example.authservice.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String BAD_USERNAME_MESSAGE = "User with this username already exists!";
    private static final String BAD_CREDENTIAL_MESSAGE = "Invalid credentials. Please provide valid username and password!";

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badUsernameException() {
        return BAD_USERNAME_MESSAGE;
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String badCredentialException() {
        return BAD_CREDENTIAL_MESSAGE;
    }
}