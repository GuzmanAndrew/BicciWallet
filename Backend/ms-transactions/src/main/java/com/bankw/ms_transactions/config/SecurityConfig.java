package com.bankw.ms_transactions.config;

import com.bankw.ms_transactions.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    return http.csrf(csrf -> csrf.disable()) // CSRF deshabilitado (vulnerabilidad)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/transactions/transfer", "/transactions/history")
                    .authenticated()) // Requiere autenticación pero sin validación real
        .addFilterBefore(
            jwtAuthenticationFilter,
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
                .class) // Agregamos el filtro inseguro
        .httpBasic(basic -> basic.disable()) // Deshabilita autenticación básica
        .build();
  }
}
