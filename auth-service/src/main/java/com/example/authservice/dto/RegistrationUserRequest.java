package com.example.authservice.dto;

import lombok.Data;

@Data
public class RegistrationUserRequest {
    private String username;
    private String password;
}
