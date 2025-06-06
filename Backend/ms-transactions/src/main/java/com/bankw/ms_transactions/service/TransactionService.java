package com.bankw.ms_transactions.service;

import com.bankw.ms_transactions.model.dto.PaginatedResponseDto;
import com.bankw.ms_transactions.model.dto.TransactionDto;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

  String processTransaction(HttpServletRequest request, String receiverUsername, BigDecimal amount);

  List<TransactionDto> getTransactionHistory(HttpServletRequest request);

  PaginatedResponseDto<TransactionDto> getAllTransactionsPaginated(
      HttpServletRequest request, int page, int size);

  String getForObject(String url);
}
