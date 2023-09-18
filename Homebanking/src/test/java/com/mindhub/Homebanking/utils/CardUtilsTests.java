package com.mindhub.Homebanking.utils;

import com.mindhub.Homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void testGenerateCardNumber() {
        // Generate a random card number
        String cardNumber = CardUtils.generateRandomCardNumber();

        // Verify that the generated string is not null or empty
        assertNotNull(cardNumber);
        assertFalse(cardNumber.isEmpty());
    }

    @Test
    public void testFormatCardNumberGenerate() {
        // Generate a random card number
        String cardNumber = CardUtils.generateRandomCardNumber();

        // Verify that the string is in the correct format
        // (four groups of four digits separated by dashes)
        //xxxx-xxxx-xxxx-xxxx
        assertTrue(cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }

    //cvv test

    @Test
    public void testGetCVVValid() {
        // generate valid CVV
        int cvv = CardUtils.generateRandomCVV();

        // Verify that the generated CVV is a three-digit number
        assertTrue(cvv >= 100 && cvv <= 999);
    }

    @Test
    public void testGenerateInvalidFourDigitCVV() {

        //Generate a CVV with 4 digits (should be invalid)
        int cvv = 1234;

        // Verify that the CVV has four digits instead of three
        String cvvString = String.valueOf(cvv);
        assertNotEquals(3, cvvString.length());
    }
}
