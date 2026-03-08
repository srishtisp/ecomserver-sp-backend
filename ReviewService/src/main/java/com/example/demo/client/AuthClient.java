package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SpringSecurityService", url = "${services.auth}")
public interface AuthClient {

    @GetMapping("/auth/internal/user")
    InternalUserResponse getUser(@RequestParam("username") String username);

    class InternalUserResponse {
        public Long userId;
        public String username;
        public String role;
    }
}