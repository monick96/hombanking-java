package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.AccountDTO;
import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.services.AccountService;
import com.mindhub.Homebanking.services.ClientService;
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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        return accountService.getListAccountDTO();

    }

    //@RequestMapping("/accounts/{id}") only the authenticated client owning the account with that id can access the data
    //this route is no longer available to the admin, he can only see the "/accounts" route
    @RequestMapping("/accounts/{id}")
    public ResponseEntity <Object>getAccount(@PathVariable Long id, Authentication authentication) {

        //checks if the user is authenticated
        //(authentication == null) checks if a user has not tried to authenticate at all
        //(authentication.isAuthenticated()) checks if a user tried to authenticate but failed,
        // authentication could be an unauthenticated object.
        if (authentication == null || !authentication.isAuthenticated()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, login required");

        }

        //get the optional account by ID
        Optional <Account> accountOptional = accountService.getOptionalAccountById(id);

        //accountOptional.isPresent() checks if values are absent or null.
        if (accountOptional.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account with this ID not found");

        }

        //get account
        Account account = accountOptional.get();

        //get authenticated client
        Client authenticadedClient = clientService.getClientByEmail(authentication.getName());

        //checks if the authenticated client has associated the account that consults
        if (account.getClient().equals(authenticadedClient)){

            AccountDTO accountDTO = accountService.getAccountDTO(account);

            return ResponseEntity.ok(accountDTO);

        }else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this information.");

        }

    }

    //create get request to "/clients/current/accounts"
    @RequestMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication){

        //get authenticated client
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());

        if (authenticatedClient == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, login required");
        }

        // If the client is authenticated, get their accounts
        List<Account> clientAccounts = accountService.getAccountsByClient(authenticatedClient);

        if (clientAccounts == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No accounts found");

        }

        // Convert Account objects to AccountDTO
        List<AccountDTO> accountDTOs = accountService.mapToAccountDTOList(clientAccounts);


        return ResponseEntity.ok(accountDTOs);

    }

    //create account
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        // Look up the client by the authenticated username (I get email as username)
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());

        if (authenticatedClient != null) {

            // get the list of authenticated client accounts
            List<Account> accounts = accountService.getAccountsByClient(authenticatedClient);

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
                String accountNumber = number;
                accountNumberExists = clientService.getClientsList().stream()
                                .anyMatch(client -> client.getAccounts().stream()
                                .anyMatch(account -> account.getNumber().equals(accountNumber)));

            } while (accountNumberExists);

            //create new client account
            Account newAccount = accountService.createAccount(number, LocalDate.now(), 0.0);

            // Associate the account with the client
            authenticatedClient.addAccount(newAccount);

            //save account
            accountService.saveAccount(newAccount);


            return ResponseEntity.status(HttpStatus.CREATED).body("Account created");


        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");

        }

    }
}

