package com.example.demo.security;

import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // MUST be identical to the security service
    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // extract role claim
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // parse token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // optional: validate token
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}