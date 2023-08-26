package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.AccountDTO;
import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.AccountRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository
                .findAll()
                .stream()
                .map(account -> new AccountDTO(account))//o -> map(ClientDTO::new)
                .collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    //agregar condicinal y usar autentication
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(account -> new AccountDTO(account))
                .orElse(null);
    }

    //create account
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        //authentication object verification
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        // Look up the client by the authenticated username (I get email as username)
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        if (authenticatedClient != null) {

            // get the list of authenticated client accounts
            List<Account> accounts = accountRepository.findByClient(authenticatedClient);

            // Check if the customer already has 3 accounts
            if (accounts.size() >= 3) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only have up to three accounts.");
            }

            //create aleatory account number
            // Generate the random account number
            Random random = new Random();
            boolean accountNumberExists;
            String number;

            do {
                // Generate a random number between 100000 and 999999
                int numRandom = random.nextInt(900000) + 100000;
                number = "VIN-" + numRandom;

                // Check if the number is already in use on clients
                String finalNumber = number;
                accountNumberExists = clientRepository.findAll().stream()
                        .anyMatch(client -> client.getAccounts().stream()
                                .anyMatch(account -> account.getNumber().equals(finalNumber)));

                if (!accountNumberExists) {
                    //create new client account
                    Account newAccount = new Account(finalNumber, LocalDate.now(), 0.0);

                    // Associate the account with the client
                    authenticatedClient.addAccount(newAccount);

                    //save account
                    accountRepository.save(newAccount);
                }


            } while (accountNumberExists);


            return ResponseEntity.status(HttpStatus.CREATED).body("Account created");


        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");

        }

    }
}

