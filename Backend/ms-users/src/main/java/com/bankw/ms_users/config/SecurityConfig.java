package com.bankw.ms_users.config;

import com.bankw.ms_users.entities.User;
import com.bankw.ms_users.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
public class SecurityConfig {

  private final UserRepository userRepository;

  public SecurityConfig(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
      if (userOptional.isEmpty()) {
        throw new RuntimeException("User not found");
      }
      User user = userOptional.get();
      return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
          .password(user.getPassword())
          .roles("USER") // ⚠️ Vulnerabilidad: Todos los usuarios tienen el mismo rol
          .build();
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // ⚠️ CSRF deshabilitado (vulnerabilidad intencional)
        .headers(
            headers ->
                headers.frameOptions(
                    frameOptions -> frameOptions.disable())) // ✅ Permitir iframes para H2 Console
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/h2-console/**", "/webjars/**")
                    .permitAll() // ✅ Permitir acceso a H2 Console y recursos estáticos
                    .requestMatchers("/users/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated());
    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(
        userDetailsService()); // ✅ Se usa el método `userDetailsService()`
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
