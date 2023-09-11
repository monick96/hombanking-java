package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    void deleteTransactions(Set<Transaction> transactions);

    Transaction createTransaction(TransactionType type, double amount, String description, LocalDateTime date);

    List<Transaction>  getTransactionsByDateRange(Long accountId, LocalDate startDate, LocalDate endDate);
}
