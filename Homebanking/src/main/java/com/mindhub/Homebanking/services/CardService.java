package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.models.Card;
import com.mindhub.Homebanking.models.CardColor;
import com.mindhub.Homebanking.models.CardType;

import java.time.LocalDate;

public interface CardService {

    void saveCard(Card card);

    boolean cardExistsByNumber(String number);

    Card getCardByNumber(String number);

    boolean cardExistsByCvv(int cvv);

    Card createCard(CardType type, CardColor color, String number, LocalDate fromDate, LocalDate thruDate, int cvv, String cardHolder);




}
