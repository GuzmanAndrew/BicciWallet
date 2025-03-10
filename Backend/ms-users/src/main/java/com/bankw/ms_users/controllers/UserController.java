package com.bankw.ms_users.controllers;

import com.bankw.ms_users.config.JwtUtil;
import com.bankw.ms_users.entities.User;
import com.bankw.ms_users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/register")
  public User register(@RequestBody User user) { // ⚠️ No hay validación de datos
    return userService.register(user);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(
          @RequestParam String username, @RequestParam String password) {
    Optional<User> userOptional = userService.login(username, password);

    if (userOptional.isPresent()) {
      User user = userOptional.get();// ⚠️ Posible fuga de información si la consulta es lenta
      String token = jwtUtil.generateToken(user.getUsername());
      return ResponseEntity.ok(
              Collections.singletonMap("token", token));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Collections.singletonMap("error", "Invalid credentials"));
  }
}
