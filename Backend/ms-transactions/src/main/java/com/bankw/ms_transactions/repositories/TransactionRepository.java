package com.bankw.ms_transactions.repositories;

import com.bankw.ms_transactions.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findBySenderUsernameOrReceiverUsername(
      String senderUsername, String receiverUsername);
}
