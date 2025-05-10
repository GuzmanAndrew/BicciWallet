package com.bankw.ms_accounts.config;

import com.bankw.ms_accounts.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .headers(
            headers ->
                headers.frameOptions(
                    frameOptions ->
                        frameOptions.disable()))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/accounts/create").permitAll().anyRequest().authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
