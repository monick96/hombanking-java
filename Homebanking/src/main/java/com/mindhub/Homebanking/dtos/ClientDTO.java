package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
public class ClientDTO {
    //properties
    private Long id;
    private Set<AccountDTO> accounts = new HashSet<>();
    private String firstName;
    private String lastName;
    private String email;
    private Set<ClientLoanDTO> loans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();


    //class constructors
    public ClientDTO() {}

    public ClientDTO(Client client) {
        this.id = client.getId();
        // Create a new HashSet instance and add each AccountDTO
        this.accounts = client.getAccounts()
                .stream()
                .filter(Account::isActive)
                .map(account -> new AccountDTO(account))
                .collect(toSet());
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.loans = client.getClientLoans()
                .stream()
                .map(clientLoan -> new ClientLoanDTO(clientLoan))
                .collect(Collectors.toSet());
        this.cards = client.getCards()
                .stream()
                .map(card -> new CardDTO(card))
                .collect(Collectors.toSet());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
