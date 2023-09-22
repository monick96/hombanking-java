package com.mindhub.Homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses an automatic ID generation strategy
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private boolean active = true;
    @Enumerated(EnumType.STRING)
    private TypeAccount typeAccount;
    //where SA is saving account and CA is current account

    // The "client" property represents a many-to-one relationship with the "Client" entity.
    // It establishes a connection between an account and the client it belongs to.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    // Establishes a one-to-many relationship between the Account entity and the Transaction entity.
    // The "mappedBy" attribute indicates that the relationship is mapped by the "account" field in the Transaction entity.
    @OneToMany(mappedBy ="account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    //Methods
    //class constructors
    // Parameterless constructor
    public Account() {
    }

    // Constructor with parameters to initialize all attributes
    public Account(String number, LocalDate creationDate, double balance, TypeAccount typeAccount,boolean active) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.typeAccount= typeAccount;
        this.active = active;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TypeAccount getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(TypeAccount typeAccount) {
        this.typeAccount = typeAccount;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //@JsonIgnore //bad practice, there are other ways to fix it, change for DTO design pattern...
    public Client getClient() {return client;}
    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
}
