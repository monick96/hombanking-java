package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toSet;

public class LoanDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments = new ArrayList<>();

    //constructors
    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = new ArrayList<>(loan.getPayments());
    }

    //accessors

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
