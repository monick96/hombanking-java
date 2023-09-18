package com.mindhub.Homebanking.services.implement;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TransactionType;
import com.mindhub.Homebanking.repositories.TransactionRepository;
import com.mindhub.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void saveAllTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    @Override
    public void deactivateTransactions(List<Transaction> transactions) {

        for (Transaction transaction : transactions) {
            transaction.setActive(false);
        }

    }

    @Override
    public Transaction createTransaction(TransactionType type, double amount, String description, LocalDateTime date, boolean active) {

        return new Transaction(type,amount,description,date,true);

    }

    @Override
    public List<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {

        return transactionRepository.findByAccountIdAndDateBetween(accountId, startDate, endDate);

    }

    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccountAndAccountActiveIsTrue(account);
    }
}
