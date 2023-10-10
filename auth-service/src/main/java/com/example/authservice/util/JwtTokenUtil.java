package com.example.authservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    private static final String JWT_CLAIM_USERNAME = "username";
    private static final String JWT_CLAIM_ROLE = "role";

    public String generateAccessToken(UserDetails user) {
        return JWT.create()
                .withClaim(JWT_CLAIM_USERNAME, user.getUsername())
                .withClaim(JWT_CLAIM_ROLE, user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + lifetime.toMillis()))
                .sign(Algorithm.HMAC256(secret));
    }
}
