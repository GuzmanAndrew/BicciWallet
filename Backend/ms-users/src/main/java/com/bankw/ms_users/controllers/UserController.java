package com.bankw.ms_users.controllers;

import com.bankw.ms_users.config.JwtUtil;
import com.bankw.ms_users.model.dto.UpdateUserRequestDto;
import com.bankw.ms_users.model.dto.UserProfileDto;
import com.bankw.ms_users.model.entities.User;
import com.bankw.ms_users.exception.UserNotFoundException;
import com.bankw.ms_users.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

  private final UserService userService;
  private final JwtUtil jwtUtil;

  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

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

  @GetMapping("/profile")
  public ResponseEntity<UserProfileDto> getProfile(HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7);
    String username = jwtUtil.extractUsername(token);
    return ResponseEntity.ok(userService.getUserProfile(username));
  }

  @PatchMapping("/update")
  public ResponseEntity<Map<String, String>> updateUser(
      @RequestHeader("Authorization") String token, @RequestBody UpdateUserRequestDto request) {
    return ResponseEntity.ok(userService.updateUser(request));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser(
      @RequestHeader("Authorization") String token, @RequestParam String username) {
    userService.deleteByUsername(username);
    return ResponseEntity.ok("Usuario eliminado");
  }
}
