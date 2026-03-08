package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final com.example.demo.security.JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(auth -> auth

    	    // public browsing
    	    .requestMatchers(HttpMethod.GET, "/products/view/**").permitAll()
    	    .requestMatchers(HttpMethod.GET, "/products/view").permitAll()

    	    // admin actions
    	    .requestMatchers(HttpMethod.PUT, "/products/approve/**").hasRole("ADMIN")
    	    .requestMatchers(HttpMethod.PUT, "/products/disapprove/**").hasRole("ADMIN")

    	    // vendor/admin product management
    	    .requestMatchers(HttpMethod.POST, "/products/**").hasAnyRole("VENDOR","ADMIN")
    	    .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole("VENDOR","ADMIN")
    	    .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("VENDOR","ADMIN")
    	    .requestMatchers(HttpMethod.PATCH, "/products/**").hasAnyRole("VENDOR","ADMIN")

    	    .anyRequest().authenticated()
    	);

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}