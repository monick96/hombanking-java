package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.CardPayDTO;
import com.mindhub.Homebanking.models.*;
import com.mindhub.Homebanking.repositories.AccountRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.repositories.TransactionRepository;
import com.mindhub.Homebanking.services.AccountService;
import com.mindhub.Homebanking.services.CardService;
import com.mindhub.Homebanking.services.ClientService;
import com.mindhub.Homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    CardService cardService;



    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            @RequestParam double amount, @RequestParam String description,
            Authentication authentication) {

        // Check that the parameters are not empty
        if (fromAccountNumber.isBlank() || toAccountNumber.isBlank() || amount <= 0 ){

            String missingField = "";

            if (fromAccountNumber.isBlank()) {

                missingField = "From Account Number";

            } else if (toAccountNumber.isBlank()) {

                missingField = "To Account Number";

            } else if (amount <= 0) {

                missingField = "The amount cannot be less than or equal to zero";

            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(missingField + " is missing");

        }

        // Check that the account numbers are not the same
        if (fromAccountNumber.equals(toAccountNumber)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The source account is the same as the destination account");

        }

        // Get the authenticated client
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());

        //Find the source account using the account number
        Optional<Account> optionalOriginAccount = accountService.getOptionalAccountByNumber(fromAccountNumber);

        //Check if the optionalOriginAccount is not present (!optionalSourceAccount.isPresent())
        // or if the present account does not belong
        // to the authenticated client (!optionalOriginAccount.get().getClient().equals(authenticatedClient))
        if (!optionalOriginAccount.isPresent() || !optionalOriginAccount.get().getClient().equals(authenticatedClient)){

            if (optionalOriginAccount == null) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This account does not exist");

            }else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this account");

            }

        }

        //get the origin account
        Account originAccount = optionalOriginAccount.get();

        //// Verify that the destination account exists
        Optional<Account> optionalDestinationAccount = accountService.getOptionalAccountByNumber(toAccountNumber);

        if (optionalDestinationAccount.isEmpty()){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not exist");

        }

        //get the destination account
        Account destinationAccount = optionalDestinationAccount.get();

        // Check that the origin account has the amount available
        if (originAccount.getBalance() < amount) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the funds to complete this transaction");

        }

        //create transactions
        Transaction debitTransaction = transactionService.createTransaction(TransactionType.DEBIT,amount,description + " (DEBIT " + "to " + toAccountNumber + ")", LocalDateTime.now(),true);
        Transaction creditTransaction = transactionService.createTransaction(TransactionType.CREDIT,amount,description + " (CREDIT " + "from "+ fromAccountNumber + ")", LocalDateTime.now(),true);

        //link transaction with account
        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        //Update amounts and save transactions in the repository
        //subtraction from the origin account
        originAccount.setBalance(originAccount.getBalance() - amount);

        //add to the destination account
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);
        accountService.saveAccount(originAccount);
        accountService.saveAccount(destinationAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully.");

    }


    @Transactional
    @PostMapping("/transactions/pay")
    public ResponseEntity<Object> payWithCard(@RequestBody CardPayDTO cardPayDTO){

        if (cardPayDTO == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("All data must be provided. Please fill in all the required fields.");

        }

        if (cardPayDTO.getDescription() == null || cardPayDTO.getDescription().isEmpty()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Description is required");

        }

        if (cardPayDTO.getNumber() == null || cardPayDTO.getNumber().isEmpty()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Card number is required");

        }

        //verify cvv format
        if (cardPayDTO.getCvv() < 100 || cardPayDTO.getCvv() > 999) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The CVV must be a 3-digit integer.");

        }

        if (cardPayDTO.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Amount must be greater than zero");
        }

        // Check the card number format (xxxx-xxxx-xxxx-xxxx)
        String cardNumber = cardPayDTO.getNumber();

        //create pattern to compare
        Pattern cardNumberPattern = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");

        //Create a Matcher object that allows you to perform regular expression matches
        // on the cardNumber string. It is initialized with the card number.
        Matcher cardNumberMatcher = cardNumberPattern.matcher(cardNumber);

        //The matches() method is a method provided by the Matcher class of
        // the java.util.regex library. It is used to check if the entire
        // string matches the pattern defined by a regular expression.
        if (!cardNumberMatcher.matches()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid card format. Please use the format: xxxx-xxxx-xxxx-xxxx");

        }


        //verify this card exist in our database and get expiration date from card
        if (!cardService.cardExistsByNumber(cardPayDTO.getNumber())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid card number,verify that the numbers entered are correct");

        }

        //get card by number
        Card card = cardService.getCardByNumber(cardPayDTO.getNumber());

        //get current date
        LocalDate currentDate = LocalDate.now();

        //verify card expiration date
        if (card.getThruDate().isBefore(currentDate)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The card has expired. Please use a valid card.");

        }

        ///verify that card have associated de cvv entered
        if (!(card.getNumber().equals(cardPayDTO.getNumber())) || (card.getCvv() != cardPayDTO.getCvv())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The entered card number or CVV does not match our records.");

        }

        //obtain the client account associated with the card
        Set <Account> accounts = card.getClient().getAccounts()
                .stream()
                .filter(Account::isActive) // Filtra solo las cuentas activas
                .collect(Collectors.toSet());

        List<Account> accountList = new ArrayList<>(accounts);

        if (accountList.isEmpty()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No active accounts are associated with this card.");

        }

        Account firstAccount = accountList.get(0);
        System.out.println(firstAccount.getId());

        if (firstAccount.getBalance() < cardPayDTO.getAmount()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient funds");

        }

        //create transaction
        Transaction payTransaction = transactionService.createTransaction(TransactionType.DEBIT,cardPayDTO.getAmount(),cardPayDTO.getDescription(),LocalDateTime.now(),true);

        //associated transaction with account
        firstAccount.addTransaction(payTransaction);

        //set account balance
        firstAccount.setBalance(firstAccount.getBalance() - payTransaction.getAmount());

        //saves
        transactionService.saveTransaction(payTransaction);

        accountService.saveAccount(firstAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction was successful.");

    }

}
