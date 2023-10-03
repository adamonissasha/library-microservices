package com.example.authservice.service;

import com.example.authservice.dto.JwtResponse;

public interface AuthService {
    JwtResponse createToken(String username, String password);
    void createNewUser(String username, String password);
}
