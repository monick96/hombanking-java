package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.models.Card;
import com.mindhub.Homebanking.models.CardColor;
import com.mindhub.Homebanking.models.CardType;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.CardRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
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
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
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

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

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
                cvv = generateRandomCVV();
                cardNumber = generateRandomCardNumber();

                // Check if the generated card number or CVV already exist in the card repository
            } while (cardRepository.existsByNumber(cardNumber) || cardRepository.existsByCvv(cvv));

            // Create and persist new card
            Card newCard = new Card(cardType,cardColor,cardNumber, LocalDate.now(),LocalDate.now().plusYears(5),cvv,authenticatedClient.getFirstName() + " " + authenticatedClient.getLastName());
            cardRepository.save(newCard);

            //associate client with card and save in ClientRepository
            authenticatedClient.addCard(newCard);
            clientRepository.save(authenticatedClient);

            return ResponseEntity.status(HttpStatus.CREATED).body("The card was created successfully");

        }else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");

        }

    }

    //generateRandomCardNumber()
    //Create a Random object to generate random numbers
    //Use a StringBuilder to concatenate the parts of the number
    //Generates 4 groups of 4 random digits (0-9999)
    //Format each number to 4 digits with leading zeros
    //Add "-" between each group except the last one
    //Returns the String result
    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<4; i++) {
            int n = random.nextInt(9999);
            sb.append(String.format("%04d", n));
            if(i < 3) {
                sb.append("-");
            }
        }

        return sb.toString();
    }

    private int generateRandomCVV() {
        Random random = new Random();
        return random.nextInt(999);
    }
}
