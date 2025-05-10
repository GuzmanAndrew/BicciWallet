package com.bankw.ms_transactions.service.impl;

import com.bankw.ms_transactions.config.JwtUtil;
import com.bankw.ms_transactions.exception.BusinessException;
import com.bankw.ms_transactions.model.dto.PaginatedResponseDto;
import com.bankw.ms_transactions.model.dto.TransactionDto;
import com.bankw.ms_transactions.model.entities.Transaction;
import com.bankw.ms_transactions.repository.TransactionRepository;
import com.bankw.ms_transactions.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final RestTemplate restTemplate;
  private final JwtUtil jwtUtil;

  public TransactionServiceImpl(
      TransactionRepository transactionRepository, RestTemplate restTemplate, JwtUtil jwtUtil) {
    this.transactionRepository = transactionRepository;
    this.restTemplate = restTemplate;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public String processTransaction(
      HttpServletRequest request, String receiverUsername, BigDecimal amount) {
    String senderUsername = extractUsernameFromRequest(request);
    System.out.println("Procesando transferencia de " + senderUsername);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", request.getHeader("Authorization"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    String senderBalanceUrl = "http://localhost:8082/accounts/v2/balance";
    ResponseEntity<Map> senderResponse =
            restTemplate.exchange(senderBalanceUrl, HttpMethod.GET, entity, Map.class);
    Map senderAccount = senderResponse.getBody();

    if (senderAccount == null || !senderAccount.containsKey("balance")) {
      throw new BusinessException("Cuenta del remitente no encontrada.", "SENDER_NOT_FOUND");
    }

    BigDecimal senderBalance = new BigDecimal(senderAccount.get("balance").toString());
    BigDecimal amountToSend = amount;

    if (senderBalance.compareTo(amountToSend) < 0) {
      throw new BusinessException("Fondos insuficientes.", "INSUFFICIENT_FUNDS");
    }

    String receiverBalanceUrl =
            "http://localhost:8082/accounts/v1/balance?username=" + receiverUsername;
    ResponseEntity<Map> receiverResponse =
            restTemplate.exchange(receiverBalanceUrl, HttpMethod.GET, entity, Map.class);
    Map receiverAccount = receiverResponse.getBody();

    if (receiverAccount == null) {
      throw new BusinessException("Cuenta del receptor no encontrada.", "RECEIVER_NOT_FOUND");
    }

    String updateSenderUrl =
        "http://localhost:8082/accounts/update-balance?amount=-"
            + amount
            + "&username="
            + senderUsername;
    restTemplate.exchange(updateSenderUrl, HttpMethod.POST, entity, Void.class);

    String updateReceiverUrl =
        "http://localhost:8082/accounts/update-balance?amount="
            + amount
            + "&username="
            + receiverUsername;
    restTemplate.exchange(updateReceiverUrl, HttpMethod.POST, entity, Void.class);

    Transaction transaction = new Transaction();
    transaction.setSenderUsername(senderUsername);
    transaction.setReceiverUsername(receiverUsername);
    transaction.setAmount(amount);
    transactionRepository.save(transaction);

    return "Transferencia completada.";
  }

  @Override
  public List<TransactionDto> getTransactionHistory(HttpServletRequest request) {
    String username = extractUsernameFromRequest(request);

    List<Transaction> transactions =
        transactionRepository.findTop5BySenderUsernameOrReceiverUsernameOrderByTimestampDesc(
            username, username);

    return formatTransactions(transactions, username, request);
  }

  @Override
  public PaginatedResponseDto<TransactionDto> getAllTransactionsPaginated(
      HttpServletRequest request, int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

    Page<Transaction> transactionPage =
        transactionRepository.findBySenderUsernameOrReceiverUsername(
            extractUsernameFromRequest(request), extractUsernameFromRequest(request), pageable);

    Page<TransactionDto> mappedPage =
        transactionPage.map(
            tx -> formatTransaction(tx, extractUsernameFromRequest(request), request));

    return new PaginatedResponseDto<>(mappedPage);
  }

  @Override
  public String getForObject(String url) {
    return restTemplate.getForObject(url, String.class);
  }

  private String extractUsernameFromRequest(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Error: Token no proporcionado o inválido.");
    }
    return jwtUtil.extractUsername(authHeader.substring(7));
  }

  private List<TransactionDto> formatTransactions(
      List<Transaction> transactions, String username, HttpServletRequest request) {
    return transactions.stream()
        .map(tx -> formatTransaction(tx, username, request))
        .collect(Collectors.toList());
  }

  private TransactionDto formatTransaction(
      Transaction tx, String username, HttpServletRequest request) {
    BigDecimal amount = tx.getAmount();
    if (username.equals(tx.getSenderUsername())) {
      amount = amount.negate();
    }

    TransactionDto dto = new TransactionDto();
    dto.setSenderUsername(tx.getSenderUsername());
    dto.setReceiverUsername(tx.getReceiverUsername());
    dto.setAmount(amount);
    dto.setTimestamp(tx.getTimestamp());

    dto.setRawToken(request.getHeader("Authorization"));

    return dto;
  }
}
