package com.example.authservice.controller;

import com.example.authservice.dto.JwtRequest;
import com.example.authservice.dto.RegistrationUserRequest;
import com.example.authservice.exception.UsernameAlreadyExistsException;
import com.example.authservice.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create JWT token", description = "Create an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful. Returns a JWT token."),
            @ApiResponse(responseCode = " 401", description = "Invalid credentials. Authentication failed.")})
    public ResponseEntity<?> createAuthToken(@RequestBody
                                             @Parameter(description = "Authentication request containing username and password.",
                                                     required = true)
                                             JwtRequest authRequest) {
        try {
            return ResponseEntity.ok(userService.createToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/registration")
    @Operation(summary = "New user registration", description = "Registration of new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User registration successful."),
            @ApiResponse(responseCode = "400", description = "Bad request. User with the same username already exists.")})
    public ResponseEntity<?> createNewUser(@RequestBody
                                           @Parameter(description = "Registration request containing username and password.",
                                                   required = true) RegistrationUserRequest registrationUserRequest) {
        try {
            userService.createNewUser(registrationUserRequest.getUsername(), registrationUserRequest.getPassword());
            return ResponseEntity.noContent().build();
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
