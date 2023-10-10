package com.example.authservice.controller;

import com.example.authservice.dto.request.JwtRequest;
import com.example.authservice.dto.request.NewUserRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.dto.response.NewUserResponse;
import com.example.authservice.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl userService;

    @PostMapping("/registration")
    @Operation(summary = "New user registration", description = "Registration of new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User registration successful."),
            @ApiResponse(responseCode = "400", description = "Bad request. User with the same username already exists.")})
    @ResponseStatus(HttpStatus.OK)
    public NewUserResponse createNewUser(@RequestBody
                                         @Parameter(description = "Registration request containing username and password.",
                                                 required = true) NewUserRequest newUserRequest) {
        return userService.createNewUser(newUserRequest);
    }

    @PostMapping("/")
    @Operation(summary = "Create JWT token", description = "Create an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful. Returns a JWT token."),
            @ApiResponse(responseCode = " 401", description = "Invalid credentials. Authentication failed.")})
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse createAuthToken(@RequestBody
                                       @Parameter(description = "Authentication request containing username and password.",
                                               required = true)
                                       JwtRequest authRequest) {
        return userService.createToken(authRequest);
    }
}
