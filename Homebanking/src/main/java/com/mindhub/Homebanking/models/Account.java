package com.mindhub.Homebanking.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Account {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses an automatic ID generation strategy
    private Long id;
    private String number;
    private LocalDate creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    // The "client" property represents a many-to-one relationship with the "Client" entity.
    // It establishes a connection between an account and the client it belongs to.

    //Methods
    //class constructors
    // Parameterless constructor
    public Account() {
    }

    // Constructor with parameters to initialize all attributes
    public Account(String number, LocalDate creationDate, double balance, Client client) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
