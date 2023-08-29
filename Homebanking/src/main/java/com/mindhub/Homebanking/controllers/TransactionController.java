package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TransactionType;
import com.mindhub.Homebanking.repositories.AccountRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            @RequestParam long amount, @RequestParam String description,
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
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        //Find the source account using the account number
        Optional<Account> optionalOriginAccount = accountRepository.findByNumber(fromAccountNumber);

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
        Optional<Account> optionalDestinationAccount = accountRepository.findByNumber(toAccountNumber);

        if (!optionalDestinationAccount.isPresent() ){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not exist");

        }

        //get the destination account
        Account destinationAccount = optionalDestinationAccount.get();

        // Check that the origin account has the amount available
        if (originAccount.getBalance() < amount) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the funds to complete this transaction");

        }

        //create transactions
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,amount,description + " (DEBIT " + fromAccountNumber + ")", LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,amount,description + " (CREDIT " + toAccountNumber + ")", LocalDateTime.now());

        //link transaction with account
        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);

        //Update amounts and save transactions in the repository
        //subtraction from the origin account
        originAccount.setBalance(originAccount.getBalance() - amount);

        //add to the destination account
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully.");

    }

}
