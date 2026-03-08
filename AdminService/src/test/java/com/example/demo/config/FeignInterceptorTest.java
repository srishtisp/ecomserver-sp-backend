package com.example.demo.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

class FeignInterceptorTest {

    @Test
    void testAuthorizationHeaderForwarded() {

        // mock incoming HTTP request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer test-token");

        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        FeignInterceptor interceptorConfig = new FeignInterceptor();
        RequestInterceptor interceptor = interceptorConfig.requestInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().containsKey("Authorization"));
    }

    @Test
    void testNoAuthorizationHeader() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        FeignInterceptor interceptorConfig = new FeignInterceptor();
        RequestInterceptor interceptor = interceptorConfig.requestInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertFalse(template.headers().containsKey("Authorization"));
    }
    
    @Test
    void testNoRequestAttributes() {

        RequestContextHolder.resetRequestAttributes();

        FeignInterceptor interceptorConfig = new FeignInterceptor();
        RequestInterceptor interceptor = interceptorConfig.requestInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().isEmpty());
    }
}