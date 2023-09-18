package com.mindhub.Homebanking.utils;

import java.util.Random;

public  final class CardUtils {

    // Private constructor to avoid instantiation
    private CardUtils() {
    }

    //generateRandomCardNumber()
    //Create a Random object to generate random numbers
    //Use a StringBuilder to concatenate the parts of the number
    //Generates 4 groups of 4 random digits (0-9999)
    //Format each number to 4 digits with leading zeros
    //Add "-" between each group except the last one
    //Returns the String result
    public static String generateRandomCardNumber() {
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

    public static int generateRandomCVV() {
        Random random = new Random();
        return random.nextInt(999);
    }

}
