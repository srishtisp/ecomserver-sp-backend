package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;


import com.example.demo.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                	    .requestMatchers("/reviews/product/**").permitAll()
                	    .requestMatchers(HttpMethod.POST, "/reviews").hasAuthority("CUSTOMER")
                	    .requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}").hasAuthority("CUSTOMER")
                	    .requestMatchers(HttpMethod.DELETE, "/reviews/admin/**").hasAuthority("ADMIN")
                	    .requestMatchers(HttpMethod.GET, "/reviews/admin").hasAuthority("ADMIN")
                	    .requestMatchers(HttpMethod.GET, "/reviews/inbox/my").hasAuthority("CUSTOMER")
                	    .anyRequest().authenticated()
                	)

                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
