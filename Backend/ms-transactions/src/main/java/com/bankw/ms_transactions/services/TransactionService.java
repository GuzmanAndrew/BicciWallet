package com.bankw.ms_transactions.services;

import com.bankw.ms_transactions.entities.Transaction;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface TransactionService {

  String processTransaction(HttpServletRequest request, String receiverUsername, Double amount);

  List<Map<String, Object>> getTransactionHistory(HttpServletRequest request);
}
