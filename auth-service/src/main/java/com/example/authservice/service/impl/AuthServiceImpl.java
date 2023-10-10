package com.example.authservice.service.impl;

import com.example.authservice.dto.request.JwtRequest;
import com.example.authservice.dto.request.NewUserRequest;
import com.example.authservice.dto.response.JwtResponse;
import com.example.authservice.dto.response.NewUserResponse;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthService;
import com.example.authservice.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtils;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse createToken(JwtRequest jwtRequest) throws BadCredentialsException {
        String username = jwtRequest.getUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, jwtRequest.getPassword()));
        String token = generateToken(username);
        return JwtResponse.builder()
                .token(token)
                .build();
    }

    public String generateToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtils.generateAccessToken(userDetails);
    }

    @Override
    public NewUserResponse createNewUser(NewUserRequest newUserRequest) {
        String username = newUserRequest.getUsername();
        User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(newUserRequest.getPassword()))
                .build();
        newUser = userRepository.save(newUser);
        return NewUserResponse.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .token(generateToken(username))
                .build();
    }
}
