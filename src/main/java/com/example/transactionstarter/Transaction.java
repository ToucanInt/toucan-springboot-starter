package com.example.transactionstarter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
public class Transaction {
    @Id
    @NotBlank(message = "Transaction ID is required")
    @Size(min = 6, max = 36, message = "Transaction ID must be between 6 and 36 characters")
    private String transactionId;

    @NotBlank(message = "Customer ID is required")
    @Size(min = 6, max = 36, message = "Customer ID must be between 6 and 36 characters")
    private String customerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 7, fraction = 2)
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Pattern(
        regexp = "^(INR|EUR|USD)$",
        message = "Currency must be INR, EUR, or USD")
    private String currency;   

    @NotBlank(message = "Transaction type is required")
    @Pattern(
    regexp = "^(CARD|ONLINE|UPI|BANK_TRANSFER)$",
    message = "Transaction type must be CARD, ONLINE, UPI, or BANK_TRANSFER")
    private String transactionType;

    @NotBlank(message = "Transaction status is required")
    @Pattern(
    regexp = "^(PENDING|COMPLETED|FAILED)$",
    message = "Transaction status must be PENDING, COMPLETED, or FAILED")
    private String transactionStatus = "PENDING";

    public Transaction() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}