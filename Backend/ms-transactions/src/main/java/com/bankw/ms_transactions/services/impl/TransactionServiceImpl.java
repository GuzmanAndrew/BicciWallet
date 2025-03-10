package com.bankw.ms_transactions.services.impl;

import com.bankw.ms_transactions.config.JwtUtil;
import com.bankw.ms_transactions.entities.Transaction;
import com.bankw.ms_transactions.repositories.TransactionRepository;
import com.bankw.ms_transactions.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired private TransactionRepository transactionRepository;

  @Autowired private RestTemplate restTemplate;

  @Autowired private JwtUtil jwtUtil;

  @Override
  public String processTransaction(
      HttpServletRequest request, String receiverUsername, Double amount) {
    // 🔹 Extraemos el token del request
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return "Error: Token no proporcionado o inválido.";
    }

    String token = authHeader.substring(7); // Quitamos "Bearer "

    // 🔹 Extraemos el `senderUsername` del token
    String senderUsername = jwtUtil.extractUsername(token); // ⚠️ No se valida la firma del token

    System.out.println("Procesando transferencia de " + senderUsername);

    // 🔹 Se crea la cabecera con el token
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", authHeader);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // 🔹 Consulta saldo del remitente
    String senderBalanceUrl = "http://localhost:8082/accounts/find?username=" + senderUsername;
    ResponseEntity<Map> senderResponse =
        restTemplate.exchange(senderBalanceUrl, HttpMethod.GET, entity, Map.class);
    Map senderAccount = senderResponse.getBody();

    if (senderAccount == null || !senderAccount.containsKey("balance")) {
      return "Error: Cuenta del remitente no encontrada.";
    }

    Double senderBalance = (Double) senderAccount.get("balance");

    if (senderBalance < amount) {
      return "Error: Fondos insuficientes.";
    }

    String receiverBalanceUrl = "http://localhost:8082/accounts/find?username=" + receiverUsername;
    ResponseEntity<Map> receiverResponse =
        restTemplate.exchange(receiverBalanceUrl, HttpMethod.GET, entity, Map.class);
    Map receiverAccount = receiverResponse.getBody();

    if (receiverAccount == null) {
      return "Error: Cuenta del receptor no encontrada.";
    }

    // 🔹 Actualiza saldo del remitente (resta el monto)
    String updateSenderUrl =
        "http://localhost:8082/accounts/update-balance?amount=-"
            + amount
            + "&username="
            + senderUsername;
    restTemplate.exchange(updateSenderUrl, HttpMethod.POST, entity, Void.class);

    // 🔹 Actualiza saldo del receptor (suma el monto)
    String updateReceiverUrl =
        "http://localhost:8082/accounts/update-balance?amount="
            + amount
            + "&username="
            + receiverUsername;
    restTemplate.exchange(updateReceiverUrl, HttpMethod.POST, entity, Void.class);

    // 🔹 Se guarda la transacción
    Transaction transaction = new Transaction();
    transaction.setSenderUsername(senderUsername);
    transaction.setReceiverUsername(receiverUsername);
    transaction.setAmount(amount);
    transactionRepository.save(transaction);

    return "Transferencia completada.";
  }

  @Override
  public List<Map<String, Object>> getTransactionHistory(HttpServletRequest request) {
    // 🔹 Extraemos el token del request
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Error: Token no proporcionado o inválido.");
    }

    String token = authHeader.substring(7); // Quitamos "Bearer "
    String username = jwtUtil.extractUsername(token);

    List<Transaction> transactions =
        transactionRepository.findBySenderUsernameOrReceiverUsername(username, username);

    // 🔹 Modifica los montos antes de retornarlos
    return transactions.stream()
        .map(
            transaction -> {
              double amount = transaction.getAmount();

              // 🔹 Si el usuario es el remitente, el monto es negativo
              if (username.equals(transaction.getSenderUsername())) {
                amount = -amount;
              }

              // 🔹 Formateamos el monto con signo explícito y 2 decimales
              String formattedAmount = String.format("%+.2f", amount);

              // 🔹 Retornamos un mapa con los valores corregidos
              Map<String, Object> transactionMap = new HashMap<>();
              transactionMap.put("id", transaction.getId());
              transactionMap.put("senderUsername", transaction.getSenderUsername());
              transactionMap.put("receiverUsername", transaction.getReceiverUsername());
              transactionMap.put("amount", formattedAmount);
              transactionMap.put("timestamp", transaction.getTimestamp());

              return transactionMap;
            })
        .collect(Collectors.toList());
  }
}
