package com.example.libraryservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secret;
    private static final String ROLE = "ROLE_ADMIN";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_USERNAME = "username";

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        try {
            String jwtToken = extractToken(request);
            if (jwtToken != null) {
                validateToken(jwtToken);
                username = getUsername(jwtToken);
            }
        } catch (TokenExpiredException | SignatureException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Stream.of(ROLE)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    public void validateToken(String token) throws TokenExpiredException, SignatureException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .build();
        verifier.verify(token);
    }

    public String getUsername(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(JWT_CLAIM_USERNAME).asString();
    }
}
