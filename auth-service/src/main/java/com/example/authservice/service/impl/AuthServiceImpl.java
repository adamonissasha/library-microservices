package com.example.authservice.service.impl;

import com.example.authservice.dto.JwtResponse;
import com.example.authservice.exception.UsernameAlreadyExistsException;
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
    public JwtResponse createToken(String username, String password) throws BadCredentialsException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtils.generateAccessToken(userDetails);
        return new JwtResponse(token);
    }

    @Override
    public void createNewUser(String username, String password) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    throw new UsernameAlreadyExistsException("Username already exists!");
                },
                () -> userRepository.save(User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .build())
        );
    }
}
