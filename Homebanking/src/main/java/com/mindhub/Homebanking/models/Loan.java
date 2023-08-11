package com.mindhub.Homebanking.models;

import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses an automatic ID generation strategy
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private  double maxAmount;

    @ElementCollection
    @Column(name="payments")
    private Set<Integer> payments;

//    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<ClientLoan> clientLoans;

    //constructors
    public Loan() {
    }

    public Loan(String name, double maxAmount) {
        this.name = name;
        this.maxAmount = maxAmount;

    }

    public Loan(String name, double maxAmount, Set<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<Integer> getPayments() {
        return payments;
    }


    public void setPayments(Set<Integer> payments) {
        this.payments = payments;
    }
}

