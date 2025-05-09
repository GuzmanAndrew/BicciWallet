package com.bankw.ms_users.controllers;

import com.bankw.ms_users.config.JwtUtil;
import com.bankw.ms_users.entities.User;
import com.bankw.ms_users.exception.UserNotFoundException;
import com.bankw.ms_users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
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
  public User register(@RequestBody User user) {
    return userService.register(user);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(
          @RequestParam String username, @RequestParam String password) {
    try {
      Optional<User> userOptional = userService.login(username, password);
      if (userOptional.isPresent()) {
        String token = jwtUtil.generateToken(userOptional.get().getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", "Invalid credentials"));
      }
    } catch (UserNotFoundException ex) {
      Map<String, String> error = new HashMap<>();
      error.put("error", ex.getMessage());
      error.put("code", "USR500");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
  }
}
