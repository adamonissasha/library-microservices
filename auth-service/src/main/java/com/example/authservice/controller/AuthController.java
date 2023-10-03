package com.example.authservice.controller;

import com.example.authservice.dto.JwtRequest;
import com.example.authservice.dto.RegistrationUserRequest;
import com.example.authservice.exception.UsernameAlreadyExistsException;
import com.example.authservice.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl userService;

    @PostMapping("/")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            return ResponseEntity.ok(userService.createToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserRequest registrationUserRequest) {
        try {
            userService.createNewUser(registrationUserRequest.getUsername(), registrationUserRequest.getPassword());
            return ResponseEntity.noContent().build();
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("HELLO");
    }
}
