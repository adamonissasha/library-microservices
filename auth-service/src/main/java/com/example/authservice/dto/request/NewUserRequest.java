package com.example.authservice.dto.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private String username;
    private String password;
}
