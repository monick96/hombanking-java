package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.models.Card;
import com.mindhub.Homebanking.models.CardColor;
import com.mindhub.Homebanking.models.CardType;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.CardRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.services.CardService;
import com.mindhub.Homebanking.services.ClientService;
import com.mindhub.Homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity <Object> createCard (
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType,
            Authentication authentication){

        //authentication object verification
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        //verify that the cardColor and cardType parameters are not null
        if (cardColor == null || cardType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card color and type are required.");
        }

        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());

        if (authenticatedClient != null) {
            Set<Card> clientCards= authenticatedClient.getCards();

            // Check cardType and cardColor limits
            int cardsOfTypeAndColor = (int) clientCards.stream().filter(card -> card.getType() == cardType && card.getColor() == cardColor).count();

            //check to allow the client to have a card of each color in each type of card (credit and debit), avoiding repeating type-color combinations.
            if (cardsOfTypeAndColor >= 1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot exceed the limit of 1 cards of this type and color.");
            }

            int cvv;
            String cardNumber;

            do {
                // Generate random CVV and card number
                cvv = CardUtils.generateRandomCVV();
                cardNumber = CardUtils.generateRandomCardNumber();

                // Check if the generated card number or CVV already exist in the card repository
            } while (cardService.cardExistsByNumber(cardNumber) || cardService.cardExistsByCvv(cvv));

            // Create and persist new card
            Card newCard = cardService.createCard(cardType,cardColor,cardNumber, LocalDate.now(),LocalDate.now().plusYears(5),cvv,authenticatedClient.getFirstName() + " " + authenticatedClient.getLastName());
            cardService.saveCard(newCard);

            //associate client with card and save in ClientRepository
            authenticatedClient.addCard(newCard);
            clientService.saveClient(authenticatedClient);

            return ResponseEntity.status(HttpStatus.CREATED).body("The card was created successfully");

        }else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");

        }

    }


}
