package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;

import java.time.LocalDate;

public class AccountDTO {
    //properties
    private Long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    //constructor
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
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

}
