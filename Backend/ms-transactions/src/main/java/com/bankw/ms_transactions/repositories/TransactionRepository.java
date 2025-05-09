package com.bankw.ms_transactions.repositories;

import com.bankw.ms_transactions.model.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Page<Transaction> findBySenderUsernameOrReceiverUsername(
          String senderUsername, String receiverUsername, Pageable pageable);

  List<Transaction> findTop5BySenderUsernameOrReceiverUsernameOrderByTimestampDesc(
          String senderUsername, String receiverUsername);
}
