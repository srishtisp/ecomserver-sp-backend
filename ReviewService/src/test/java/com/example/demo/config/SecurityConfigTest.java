package com.example.demo.config;

import com.example.demo.security.JwtFilter;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Test
    void shouldBuildSecurityFilterChain() throws Exception {

        JwtFilter jwtFilter = mock(JwtFilter.class);

        SecurityConfig config = new SecurityConfig();

        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        SecurityFilterChain chain = config.securityFilterChain(http);

        assertNotNull(chain);
    }
}