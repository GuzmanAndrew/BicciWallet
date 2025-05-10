package com.bankw.ms_transactions.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String senderUsername;
  private String receiverUsername;

  @Column(precision = 20, scale = 2)
  private BigDecimal amount;

  private LocalDateTime timestamp = LocalDateTime.now();

}
