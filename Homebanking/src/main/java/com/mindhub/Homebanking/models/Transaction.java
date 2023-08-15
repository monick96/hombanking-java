package com.mindhub.Homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses an automatic ID generation strategy
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private TransactionType type; // enum

    private double amount;
    private String description;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    //Constructors
    public Transaction() {
    }

    public Transaction(TransactionType type, double amount, String description, LocalDateTime date) {
        this.type = type;
        // Using the ternary operator, adjust the amount based on the type of transaction
        // If it is debit, the amount becomes negative; if it's credit, it stays positive
        this.amount = (type == TransactionType.DEBIT ? -amount : amount);
        this.description = description;
        this.date = date;
    }

    //accessors

    public Long getId() {
        return id;
    }
    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
