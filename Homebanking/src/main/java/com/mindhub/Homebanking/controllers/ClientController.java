package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.AccountRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {

        return clientService.getClientsDTO();

    }


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {

            String missingField = "";

            if (firstName.isBlank()) {

                missingField = "First Name";

            } else if (lastName.isBlank()) {

                missingField = "Last Name";

            } else if (email.isBlank()) {

                missingField = "Email";

            } else if (password.isBlank()) {

                missingField = "Password";

            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(missingField + " is missing");

        }


        if (clientService.getClientFindByEmail(email) !=  null) {

            //return new ResponseEntity<>("mail already in use", HttpStatus.FORBIDDEN);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("mail already in use");

        }

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientService.saveClient(newClient);

        //create aleatory account number
        // Generate the random account number
        Random random = new Random();
        boolean accountNumberExists;
        String number;

        do{
            // Generate a random number between 100000 and 999999
            int numRandom = random.nextInt(900000) + 100000;
            number = "VIN-" + numRandom;

            // Check if the number is already in use on clients
            String finalNumber = number;
            accountNumberExists = clientService.getClientsList().stream()
                    .anyMatch(client -> client.getAccounts().stream()
                    .anyMatch(account -> account.getNumber().equals(finalNumber)));

            if (!accountNumberExists){
                //create new client account
                Account newAccount = new Account(finalNumber,LocalDate.now(),0.0);

                // Associate the account with the client
                newClient.addAccount(newAccount);

                //save account
                accountRepository.save(newAccount);
            }


        }while(accountNumberExists);


        return ResponseEntity.status(HttpStatus.CREATED).body("Successful registration");
    }


    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id, Authentication authentication) {

        Client authenticadedClient = clientService.getClientFindByEmail(authentication.getName());

        //Optional: class in Java to deal with values that may be absent or null.
        Optional<Client> optionalClient = clientService.getClientFindById(id);

        if (authenticadedClient != null && optionalClient.isPresent()){

            //get client
           Client clientById = optionalClient.get();

            // Check if the authenticated client is the same client as the received id
            if(authenticadedClient.getId().equals(clientById.getId())){

                ClientDTO clientDTO = clientService.getClientDTO(clientById);

                return ResponseEntity.ok( clientDTO);

            }else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this information.");

            }
        }

        // Authenticated client or client with ID not found

        if (authenticadedClient == null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authenticated client not found");

        }else{

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("client with this ID not found");

        }

    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {

        Client client = clientService.getClientFindByEmail(authentication.getName());

        if (client == null) {
            throw new ResourceNotFoundException("Client not found");
        }

        return new ClientDTO(client);
    }

}
