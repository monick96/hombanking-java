package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.LoanApplicationDTO;
import com.mindhub.Homebanking.dtos.LoanDTO;
import com.mindhub.Homebanking.models.*;
import com.mindhub.Homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @RequestMapping(path="/loans")
    public ResponseEntity<Object>getLoans(Authentication authentication){

        // Verify that the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");

        }

        List<Loan> loans = loanRepository.findAll();

        // Map the loans to LoanDTO
        List<LoanDTO> loanDTOs = loans.stream()
                .map(loan -> new LoanDTO(loan))
                .collect(Collectors.toList());

        return ResponseEntity.ok(loanDTOs);

    }

    @Transactional
    @RequestMapping(path="/loans", method= RequestMethod.POST)
    public ResponseEntity<Object> createLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO,
            Authentication authentication) {

        // Verify that the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");

        }

        // Verify that the data is not empty or that the amount and fees are not 0
        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    "Invalid data: the fields cannot be empty and the amount " +
                            "and fees cannot be less than or equal to zero");

        }

        // Check that the loan exists
        Optional<Loan> optionalLoan = loanRepository.findById(loanApplicationDTO.getLoanId());

        if (optionalLoan.isEmpty()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The loan does not exist");

        }

        //get loan
        Loan loan = optionalLoan.get();

        // Verify that the requested amount does not exceed the maximum loan amount
        if (loanApplicationDTO.getAmount()> loan.getMaxAmount()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Loan amount exceeds maximum.");

        }

        // Verify that the number of installments is among those available for the loan
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid number of payments");

        }

        Optional<Account> optionalAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        if (optionalAccount.isEmpty()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not exist");

        }

        // Check if the customer already has a loan with the same name
        //findByClientEmailAndLoanName(): custom query that finds loans by customer email and loan name.
        //existingLoans: Contains the list of existing loans with the same name. If this list is not empty,
        // it means that the client already has a loan with the same name
        List <ClientLoan> existingLoans = clientLoanRepository.findByClientEmailAndLoanName(authentication.getName(), loan.getName());

        if (!existingLoans.isEmpty()) {
            // The client already has a loan with this name, we return an error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You already have a loan with this name.");
        }

        //get destination account
        Account destinationAccount = optionalAccount.get();

        //Verify that the destination account belongs to the authenticated client
        //get authenticated client
        Client authenticadedClient = clientRepository.findByEmail(authentication.getName());

        //get authenticated client accounts
        List<Account> authenticatedClientAccounts = accountRepository.findByClient(authenticadedClient);

        if (!authenticatedClientAccounts.contains(destinationAccount)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This destination account does not belong to the logged in client");

        }

        //add the total amount requested plus 20% interest
        long totalAmount = (long) (loanApplicationDTO.getAmount() * 1.20);

        // Create the loan request
        ClientLoan loanRequest = new ClientLoan(authenticadedClient,loan,loanApplicationDTO.getPayments(),totalAmount);

        //add loan request to client
        authenticadedClient.addClientLoan(loanRequest);

        //create a “CREDIT” transaction
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,totalAmount,loan.getName() + "Loan approved", LocalDateTime.now());

        //Update the destination account by adding the requested amount.
        destinationAccount.setBalance(destinationAccount.getBalance() + loanRequest.getAmount());

        //save in repositories
        clientLoanRepository.save(loanRequest);
        transactionRepository.save(creditTransaction);
        accountRepository.save(destinationAccount);
        clientRepository.save(authenticadedClient);


        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully requested loan");
    }


}
