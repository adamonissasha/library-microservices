package com.example.authservice.service;

import com.example.authservice.dto.request.JwtRequest;
import com.example.authservice.dto.request.NewUserRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.dto.response.NewUserResponse;

public interface AuthService {
    JwtResponse createToken(JwtRequest jwtRequest);
    NewUserResponse createNewUser(NewUserRequest newUserRequest);
}
