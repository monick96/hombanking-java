package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TransactionType;

import java.time.LocalDateTime;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    Transaction createTransaction(TransactionType type, long amount, String description, LocalDateTime date);
}
