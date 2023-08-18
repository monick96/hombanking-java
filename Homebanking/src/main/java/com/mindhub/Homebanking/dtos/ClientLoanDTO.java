package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.ClientLoan;
import com.mindhub.Homebanking.models.Loan;

public class ClientLoanDTO {
    //properties
    private Long id;
    private Long loanId;
    private String name;
    private double amount;
    private int payments;

    //constructors
    public ClientLoanDTO(){}

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    //getters
    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}
