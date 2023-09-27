package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.AccountDTO;
import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.Transaction;
import com.mindhub.Homebanking.models.TypeAccount;
import com.mindhub.Homebanking.services.AccountService;
import com.mindhub.Homebanking.services.ClientService;
import com.mindhub.Homebanking.services.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
//    @Autowired
//    private AccountService accountService;
//
//    @Autowired
//    private ClientService clientService;
//
//    @Autowired
//    private TransactionService transactionService;

//    @Autowired
//    TemplateEngine templateEngine;

    private final AccountService accountService;

    private final ClientService clientService;

    private final TransactionService transactionService;

    public AccountController(AccountService accountService, ClientService clientService, TransactionService transactionService) {
        this.accountService = accountService;
        this.clientService = clientService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        return accountService.getListAccountDTO();

    }

    //@RequestMapping("/accounts/{id}") only the authenticated client owning the account with that id can access the data
    //this route is no longer available to the admin, he can only see the "/accounts" route
    @GetMapping("/accounts/{id}")
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

        //checks if the authenticated client has associated the account that consults and if de account is active
        if (account.getClient().equals(authenticadedClient) && account.isActive()){

            AccountDTO accountDTO = accountService.getAccountDTO(account);

            return ResponseEntity.ok(accountDTO);

        }else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this information.");

        }

    }


    //The HTTP response that returns a file, such as a
    // PDF, is typically handled by sending the file data
    // in an array of bytes (byte[]).
    // This is because binary data, such as a PDF file,
    // can be represented as a sequence of bytes.
    @GetMapping("/accounts/{id}/statement")
    //terminar//poner la comprobacion que sea cuenta activa
    public ResponseEntity<byte[]>downlandStatement(
            @PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, Authentication authentication){
        //validar que start no sea posterior a end
        //check if client login
        if (authentication == null || !authentication.isAuthenticated()) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized, login required");

        }

        //check if fields are null
        if ((id == null) || (startDate == null) || (endDate == null)) {

            String missingField = "";

            if (id == null) {

                missingField = "Id";

            } else if (startDate == null) {

                missingField = "Start Date";

            } else if (endDate == null) {

                missingField = "End Date";

            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,missingField + " are required.");

        }

        if (startDate.isAfter(endDate)) {

            throw new IllegalArgumentException("Start Date must be before End Date");

        }

        if (!endDate.isEqual(endDate) || endDate.isBefore(startDate)) {

            throw new IllegalArgumentException("End Date must be equal or after End Date");

        }

        //get the optional account by ID
        Optional <Account> accountOptional = accountService.getOptionalAccountById(id);

        //accountOptional.isPresent() checks if values are absent or null.
        if (accountOptional.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account with this ID not found");

        }

        //get account
        Account account = accountOptional.get();

        //get authenticated client
        Client authenticadedClient = clientService.getClientByEmail(authentication.getName());

        //checks if the authenticated client has associated the account that consults
        if (!account.getClient().equals(authenticadedClient)){

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not authorized to access this information.");

        }

        // Get transactions in date range
        List<Transaction> transactionList = transactionService.getTransactionsByDateRange(id,startDate,endDate);

        if (transactionList == null) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"There are no transactions in the indicated date range");

        }

//        // Generate PDF
//        // create a new document PDF
//        PDDocument document = new PDDocument();//blank pdf
//
//        // create a page for document
//        PDPage page = new PDPage(PDRectangle.A4);
//        document.addPage(page);
//
//        // Create an object that is responsible to write to the page
//        PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//        //Add content to PDF
//        //Sets the font and font size to use for the text
//
//        contentStream.setFont(PDType1Font font, 12);

//        Context context = new Context();
//        context.setVariable("account", account);
//        context.setVariable("transactions", transactionList);
//        String html = templateEngine.process("statement", context);
//
//        // Generate PDF
//        // Generar PDF
//        byte[] pdf = null;

//        try {
//
//            Document document = new Document();
//            PdfWriter writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
//
//            document.open();
//
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
//
//            document.close();
//
//            pdf = ((ByteArrayOutputStream)writer.getOutputStream()).toByteArray();
//
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating PDF");
//        }
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statement.pdf")
//                .body(pdf);


        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "This resources is in construction");


    }

    //create get request to "/clients/current/accounts"
    @RequestMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> getAccounts(Authentication authentication){

        //get authenticated client
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());

        if (authenticatedClient == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized, login required");
        }

        // If the client is authenticated, get their active accounts
        List<Account> clientAccounts = accountService.getAccountsByClient(authenticatedClient)
                .stream()
                .filter(Account::isActive)
                .collect(Collectors.toList());

        if (clientAccounts == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No accounts found");

        }

        // Convert Account objects to AccountDTO
        List<AccountDTO> accountDTOs = accountService.mapToAccountDTOList(clientAccounts);


        return ResponseEntity.ok(accountDTOs);

    }


    //create account
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam TypeAccount typeAccount, Authentication authentication) {

        // Look up the client by the authenticated username (I get email as username)
        Client authenticatedClient = clientService.getClientByEmail(authentication.getName());

        if (authenticatedClient != null) {

            // get the list of authenticated client accounts
            List<Account> accounts = accountService.getAccountsByClient(authenticatedClient)
                    .stream()
                    .filter(Account::isActive)
                    .collect(Collectors.toList());

            // Check if the customer already has 3 accounts
            if (accounts.size() >= 3) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only have up to three accounts.");
            }

            // Check if typeAccount is null
            if (typeAccount == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Type of account is required.");
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
            Account newAccount = accountService.createAccount(number, LocalDate.now(), 0.0,typeAccount,true);

            // Associate the account with the client
            authenticatedClient.addAccount(newAccount);

            //save account
            accountService.saveAccount(newAccount);


            return ResponseEntity.status(HttpStatus.CREATED).body("Account created");


        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");

        }

    }

    @Transactional
    @PatchMapping("/accounts/{id}")
    public ResponseEntity<Object> deactivateAccount(@PathVariable Long id, Authentication authentication){

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
        if (!account.getClient().equals(authenticadedClient)){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to do this action.");

        }

        if (account.getBalance() >0){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have a balance in the account, transfer the balance before deleting the account.");

        }

        //deactivate account transactions
        //obtain all transactions associated to account
        Set<Transaction> transactionSet = account.getTransactions();

        //convert transaction set to list
        List<Transaction> transactionList = new ArrayList<>(transactionSet);

        //if the list of transactions is not empty Iterate through the
        // transactions and set their "active" status to "false"
        if (!transactionList.isEmpty()) {

            transactionService.deactivateTransactions(transactionList);

        }

        //save the update transactions
        transactionService.saveAllTransactions(transactionList);


        //deactivate account
        account.setActive(false);

        accountService.saveAccount(account);

        return ResponseEntity.ok("Successfully deleted account and transactions");

    }

}

