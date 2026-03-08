package com.example.demo.security;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldContinueFilterChainWhenNoToken() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
    
    @Test
    void shouldAuthenticateWhenTokenValid() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtService.validateToken("token")).thenReturn(true);
        when(jwtService.extractUsername("token")).thenReturn("user");
        when(jwtService.extractRole("token")).thenReturn("CUSTOMER");

        jwtFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
    
    @Test
    void shouldIgnoreHeaderWithoutBearerPrefix() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic abc");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
    
    @Test
    void shouldContinueWhenTokenInvalid() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtService.validateToken("token")).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
    
    @Test
    void shouldSkipAuthenticationIfAlreadyAuthenticated() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtService.validateToken("token")).thenReturn(true);
        when(jwtService.extractUsername("token")).thenReturn("user");

        SecurityContextHolder.getContext().setAuthentication(
                mock(org.springframework.security.core.Authentication.class)
        );

        jwtFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
    
    
}