package com.example.demo.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService = new JwtService();

    @Test
    void shouldReturnFalseForInvalidToken() {

        boolean valid = jwtService.validateToken("invalid.token");

        assertFalse(valid);
    }

    @Test
    void shouldThrowExceptionForInvalidExtraction() {

        assertThrows(Exception.class, () ->
                jwtService.extractUsername("bad.token"));
    }

}