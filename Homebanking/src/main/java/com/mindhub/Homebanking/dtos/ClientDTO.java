package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;


public class ClientDTO {
    //properties
    private Long id;
    Set<AccountDTO> accounts = new HashSet<>();
    private String firstName;
    private String lastName;
    private String email;

    //class constructors


    public ClientDTO() {
    }

    public ClientDTO(Client client) {

        this.id = client.getId();
        // Create a new HashSet instance and add each AccountDTO
        this.accounts = client.getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toSet());

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
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

}
