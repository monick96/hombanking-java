package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TransactionType;

import java.time.LocalDateTime;
public class TransactionDTO {
    private Long id;
    private TransactionType type; // enum
    private double amount;
    private String description;
    private LocalDateTime date;

    private boolean active;

    //class constructors
    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.active = transaction.isActive();
    }

    // Getter
    public boolean isActive() {
        return active;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
