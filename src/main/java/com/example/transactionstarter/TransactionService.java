package com.example.transactionstarter;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Optional<Transaction> updateTransactionStatus(
        String transactionId,
        String transactionStatus) {

    Optional<Transaction> transactionOptional =
            transactionRepository.findById(transactionId);

    if (transactionOptional.isEmpty()) {
        return Optional.empty();
    }

    Transaction transaction = transactionOptional.get();

    if (!"PENDING".equals(transaction.getTransactionStatus())) {
        throw new IllegalStateException(
                "Transaction status cannot be changed from "
                        + transaction.getTransactionStatus());
    }

    if (!"COMPLETED".equals(transactionStatus)
        && !"FAILED".equals(transactionStatus)) {
        throw new IllegalArgumentException(
                "Invalid transaction status");
    }

    transaction.setTransactionStatus(transactionStatus);

    return Optional.of(transactionRepository.save(transaction));
}
    public List<Transaction> getTransactionsByCustomer(String customerId) {
    return transactionRepository.findByCustomerId(customerId);
}
}