package com.bankw.ms_transactions.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {

    private String senderUsername;
    private String receiverUsername;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String rawToken;

}
