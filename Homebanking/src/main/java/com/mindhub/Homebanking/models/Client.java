package com.mindhub.Homebanking.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public void addAccounts(Account account){
        account.setClient(this);
        accounts.add(account);
    }


}
