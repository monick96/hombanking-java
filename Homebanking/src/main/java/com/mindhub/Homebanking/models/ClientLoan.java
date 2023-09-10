package com.mindhub.Homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private int payments;

    private double amount;

    //constructor
    public ClientLoan() {
    }


    public ClientLoan(Client client, Loan loan, int payments, double amount) {
        this.client = client;
        this.loan = loan;
        this.payments = payments;
        this.amount = amount;
    }

    //accessors

    public int getPayments() {
        return payments;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Loan getLoan() {
        return loan;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

}
