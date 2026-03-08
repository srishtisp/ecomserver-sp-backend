package com.example.demo.config;

import feign.RequestTemplate;

import org.junit.jupiter.api.Test;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class FeignConfigTest {

    private FeignConfig config = new FeignConfig();

    @Test
    void shouldAddAuthorizationHeader() {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user",
                        "token123"
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        RequestTemplate template = new RequestTemplate();

        config.requestInterceptor().apply(template);

        assertTrue(template.headers().containsKey("Authorization"));
    }

    @Test
    void shouldNotAddHeaderWhenNoAuth() {

        SecurityContextHolder.clearContext();

        RequestTemplate template = new RequestTemplate();

        config.requestInterceptor().apply(template);

        assertFalse(template.headers().containsKey("Authorization"));
    }
}