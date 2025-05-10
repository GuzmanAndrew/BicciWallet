package com.bankw.ms_transactions.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

    private String senderUsername;
    private String receiverUsername;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String rawToken;

    public TransactionDto() {
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

    public String getRawToken() {
        return rawToken;
    }

    public void setRawToken(String rawToken) {
        this.rawToken = rawToken;
    }
}
