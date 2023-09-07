package com.mindhub.Homebanking.models;

import com.mindhub.Homebanking.dtos.AccountDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Client {

    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses an automatic ID generation strategy
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    // The "accounts" property represents a one-to-many relationship with the "Account" entity.
    // This establishes that a customer can have multiple accounts.
    // "fetch = FetchType.EAGER" indicates that accounts will be loaded automatically when the client is accessed.
    @OneToMany(mappedBy ="client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    //one-to-many relationship between client and loan
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    //relationship with card
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();
    private String firstName;
    private String lastName;

    private String email;

    private String password;

    private Role role;

    //class constructors

    // Parameterless constructor
    public Client() {}

    // Constructor with parameters to initialize all attributes
    public Client( String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters for attributes - accessor methods

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

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

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

}
