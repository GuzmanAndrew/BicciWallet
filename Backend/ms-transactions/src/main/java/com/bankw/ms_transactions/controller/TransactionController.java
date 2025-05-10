package com.bankw.ms_transactions.controller;

import com.bankw.ms_transactions.model.dto.PaginatedResponseDto;
import com.bankw.ms_transactions.model.dto.TransactionDto;
import com.bankw.ms_transactions.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping("/transfer")
  public ResponseEntity<Map<String, String>> transfer(
      HttpServletRequest request,
      @RequestParam String receiverUsername,
      @RequestParam BigDecimal amount) {

    String result = transactionService.processTransaction(request, receiverUsername, amount);
    return ResponseEntity.ok(Collections.singletonMap("message", result));
  }

  @GetMapping("/history")
  public ResponseEntity<List<TransactionDto>> getTransactionHistory(
      HttpServletRequest request) {
    return ResponseEntity.ok(transactionService.getTransactionHistory(request));
  }

  @GetMapping(value = "/history/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaginatedResponseDto<TransactionDto>> getAllTransactionsPaginated(
          HttpServletRequest request,
          @RequestParam int page,
          @RequestParam int size) {

    return ResponseEntity.ok(transactionService.getAllTransactionsPaginated(request, page, size));
  }

  @GetMapping("/proxy")
  public ResponseEntity<String> proxy(@RequestParam String url) {
    String body = transactionService.getForObject(url);
    return ResponseEntity.ok(body);
  }

}
