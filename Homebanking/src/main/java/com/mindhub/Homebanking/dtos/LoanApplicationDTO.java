package com.mindhub.Homebanking.dtos;

public class LoanApplicationDTO {

    //properties
    private long loanId;
    private double amount;
    private int payments;
    private String toAccountNumber;


    //default constructor
    public LoanApplicationDTO() {
    }

    //accessors - getters
    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
