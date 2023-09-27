package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.LoanApplicationDTO;
import com.mindhub.Homebanking.dtos.LoanDTO;
import com.mindhub.Homebanking.models.*;
import com.mindhub.Homebanking.repositories.*;
import com.mindhub.Homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;


    @GetMapping(path="/loans")
    public ResponseEntity<Object>getLoans(Authentication authentication){

        // Verify that the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");

        }

        List<Loan> loans = loanService.getListLoans();

        // Map the loans to LoanDTO
        List<LoanDTO> loanDTOs = loanService.mapToListLoansDTO(loans);

        return ResponseEntity.ok(loanDTOs);

    }

    @Transactional
    @PostMapping("/loans")
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
        Optional<Loan> optionalLoan = loanService.getOptionalLoanById(loanApplicationDTO.getLoanId());

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

        Optional<Account> optionalAccount = accountService.getOptionalAccountByNumber(loanApplicationDTO.getToAccountNumber());

        if (optionalAccount.isEmpty()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Destination account does not exist");

        }

        // Check if the customer already has a loan with the same name
        //findByClientEmailAndLoanName(): custom query that finds loans by customer email and loan name.
        //existingLoans: Contains the list of existing loans with the same name. If this list is not empty,
        // it means that the client already has a loan with the same name
        List <ClientLoan> existingLoans = clientLoanService.getClientLoanByEmailAndLoanName(authentication.getName(), loan.getName());

        if (!existingLoans.isEmpty()) {
            // The client already has a loan with this name, we return an error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You already have a loan with this name.");
        }

        //get destination account
        Account destinationAccount = optionalAccount.get();

        //Verify that the destination account belongs to the authenticated client
        //get authenticated client
        Client authenticadedClient = clientService.getClientByEmail(authentication.getName());

        //get authenticated client accounts
        List<Account> authenticatedClientAccounts = accountService.getAccountsByClient(authenticadedClient);

        if (!authenticatedClientAccounts.contains(destinationAccount)) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This destination account does not belong to the logged in client");

        }

        //add the total amount requested plus 20% interest
        double totalAmount = (loanApplicationDTO.getAmount() * 1.20);

        // Create the loan request
        ClientLoan loanRequest = clientLoanService.createClientLoan(authenticadedClient,loan,loanApplicationDTO.getPayments(),totalAmount);

        //add loan request to client
        authenticadedClient.addClientLoan(loanRequest);

        //create a “CREDIT” transaction
        Transaction creditTransaction = transactionService.createTransaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(),loanRequest.getLoan().getName()+ "Loan approved", LocalDateTime.now(),true);

        //link transaction with account
        destinationAccount.addTransaction(creditTransaction);

        //Update the destination account by adding the requested amount.
        destinationAccount.setBalance(destinationAccount.getBalance() + creditTransaction.getAmount());

        //save in repositories
        clientLoanService.saveClientLoan(loanRequest);
        transactionService.saveTransaction(creditTransaction);
        accountService.saveAccount(destinationAccount);
        clientService.saveClient(authenticadedClient);


        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully requested loan");
    }


}
