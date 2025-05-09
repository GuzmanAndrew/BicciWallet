package com.bankw.ms_accounts.controllers;

import com.bankw.ms_accounts.config.JwtUtil;
import com.bankw.ms_accounts.entities.Account;
import com.bankw.ms_accounts.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired private AccountService accountService;

  @PostMapping("/create")
  public ResponseEntity<Map<String, String>> createAccount(@RequestParam String username) {
    boolean created = accountService.createAccount(username);
    if (created) {
      return ResponseEntity.ok(Collections.singletonMap("message", "Cuenta creada con éxito."));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(Collections.singletonMap("error", "El usuario ya tiene una cuenta."));
    }
  }

  @PostMapping("/update-balance")
  public ResponseEntity<?> updateBalance(
      @RequestParam String username, @RequestParam BigDecimal amount) {
    boolean updated = accountService.updateBalance(username, amount);

    if (!updated) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar saldo");
    }
    return ResponseEntity.ok("Saldo actualizado correctamente");
  }

  @GetMapping("/find")
  public Map<String, Object> findAccount(
      @RequestHeader("Authorization") String token, @RequestParam String username) {
    try (Connection conn =
            DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/wallet_db", "root", "root");
        Statement stmt = conn.createStatement()) {

      ResultSet rs = null;

      if (token != null && !token.isEmpty()) {
        // Vulnerabilidad: SQL Injection porque no usamos PreparedStatement
        rs = stmt.executeQuery("SELECT * FROM accounts WHERE username = '" + username + "'");
      }

      if (rs.next()) {
        return Map.of(
            "username", rs.getString("username"),
            "balance", rs.getDouble("balance"),
            "accountType", rs.getString("account_type") // Devuelve tipo de cuenta sin validación
            );
      }
    } catch (SQLException e) {
      return Map.of("error", "Database error: " + e.getMessage());
    }

    return Map.of("error", "Account not found");
  }
}
