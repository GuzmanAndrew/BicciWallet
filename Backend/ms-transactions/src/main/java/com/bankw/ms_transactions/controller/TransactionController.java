package com.bankw.ms_transactions.controller;

import com.bankw.ms_transactions.entities.Transaction;
import com.bankw.ms_transactions.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

  @Autowired private TransactionService transactionService;

  @PostMapping("/transfer")
  public ResponseEntity<Map<String, String>> transfer(
      @RequestHeader("Authorization") String token,
      HttpServletRequest request,
      @RequestParam String receiverUsername,
      @RequestParam Double amount) {

    String result = transactionService.processTransaction(request, receiverUsername, amount);
    return ResponseEntity.ok(Collections.singletonMap("message", result));
  }

  @GetMapping("/history")
  public ResponseEntity<List<Map<String, Object>>> getTransactionHistory(HttpServletRequest request) {
    return ResponseEntity.ok(transactionService.getTransactionHistory(request));
  }
}
