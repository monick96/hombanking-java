package com.mindhub.Homebanking.models;

import com.mindhub.Homebanking.dtos.AccountDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses an automatic ID generation strategy
    private Long id;

    @OneToMany(mappedBy ="client", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();
    // The "accounts" property represents a one-to-many relationship with the "Account" entity.
    // This establishes that a customer can have multiple accounts.
    // "fetch = FetchType.EAGER" indicates that accounts will be loaded automatically when the client is accessed.

    private String firstName;
    private String lastName;

    private String email;

    //class constructors

    // Parameterless constructor
    public Client() {
        // Default values or common initial values
        this.firstName = "";
        this.lastName = "";
        this.email = "";
    }

    // Constructor with parameters to initialize all attributes
    public Client( String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and setters for attributes - accessor methods


    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account){
        // Establishes the bidirectional relationship between the client and the account.
        // The "account" argument represents the account to be added to the client

        account.setClient(this); // Sets the client's customer as the current customer (this).
        accounts.add(account);// Add the account to the set of accounts associated with the client.
    }


}
