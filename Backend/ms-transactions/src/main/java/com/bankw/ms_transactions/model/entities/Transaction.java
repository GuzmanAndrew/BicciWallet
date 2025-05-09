package com.bankw.ms_transactions.model.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String senderUsername;
  private String receiverUsername;

  @Column(precision = 20, scale = 2)
  private BigDecimal amount;

  private LocalDateTime timestamp = LocalDateTime.now();

  public Transaction() {}

  public Transaction(
      Long id,
      String senderUsername,
      String receiverUsername,
      BigDecimal amount,
      LocalDateTime timestamp) {
    this.id = id;
    this.senderUsername = senderUsername;
    this.receiverUsername = receiverUsername;
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSenderUsername() {
    return senderUsername;
  }

  public void setSenderUsername(String senderUsername) {
    this.senderUsername = senderUsername;
  }

  public String getReceiverUsername() {
    return receiverUsername;
  }

  public void setReceiverUsername(String receiverUsername) {
    this.receiverUsername = receiverUsername;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
