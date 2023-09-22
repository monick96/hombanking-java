package com.mindhub.Homebanking.dtos;

public class CardPayDTO {
    private String number;

    private String description;

    private int cvv;

    private double amount;


    public CardPayDTO() {

    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }
}
