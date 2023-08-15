package com.mindhub.Homebanking.dtos;

import com.mindhub.Homebanking.models.Card;
import com.mindhub.Homebanking.models.CardColor;
import com.mindhub.Homebanking.models.CardType;
import com.mindhub.Homebanking.models.Client;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cvv;


    //constructors
    public CardDTO() {
    }
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.cvv = card.getCvv();
    }
    //getters

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCvv() {
        return cvv;
    }

}
