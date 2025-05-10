package com.bankw.ms_accounts.controllers;

import com.bankw.ms_accounts.entities.Account;
import com.bankw.ms_accounts.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping("/all")
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountService.getAllAccounts());
  }

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

  @GetMapping("/v1/balance")
  public ResponseEntity<Map<String, Object>> legacyBalanceEndpoint(@RequestParam String username) {
    return ResponseEntity.ok(accountService.findAccount(username));
  }

  @GetMapping("/v2/balance")
  public ResponseEntity<Map<String, Object>> secureBalance(HttpServletRequest request) {
    return ResponseEntity.ok(accountService.getAuthenticatedUserAccount(request));
  }
}
