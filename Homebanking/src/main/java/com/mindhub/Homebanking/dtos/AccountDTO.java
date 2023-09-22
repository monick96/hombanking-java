package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TypeAccount;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
public class AccountDTO {
    //properties
    private Long id;
    private Set<TransactionDTO> transactions = new HashSet<>();
    private String number;
    private LocalDate creationDate;
    private double balance;
    private boolean active;

    //customize type account
    private String typeAccount;

    //constructors
    public AccountDTO(){}

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.transactions = account.getTransactions()
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toSet());
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.typeAccount = account.getTypeAccount().getDisplayName();
        this.active = account.isActive();
    }

    // Getters


    public boolean isActive() {return active;}

    public Long getId() {
        return id;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public String getTypeAccount() {
        return typeAccount;
    }
}
