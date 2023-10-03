package com.example.authservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateAccessToken(UserDetails user) {
        return JWT.create()
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + lifetime.toMillis()))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getUsername(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    public List<String> getRoles(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("role").asList(String.class);
    }
    //    public void validateToken(String token) throws TokenExpiredException {
//        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
//                .withSubject("User details")
//                .withIssuer("sasha")
//                .build();
//        verifier.verify(token);
//    }
//
////    public User getUser() {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        String login = authentication.getName();
////        return usersRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("There is no user with this login!"));
////    }
////
////    public User getUserByLogin(String login) {
////        return usersRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("There is no user with this login!"));
////    }

}
