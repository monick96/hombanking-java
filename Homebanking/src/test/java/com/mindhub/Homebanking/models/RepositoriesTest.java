package com.mindhub.Homebanking.models;

import com.mindhub.Homebanking.dtos.AccountDTO;
import com.mindhub.Homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;



    //Loan repository test
    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }



    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    //client repository test
    @Test
    public void shouldSaveAndRetrieveClient() {
        // create an example client
        Client client = new Client("John Felix Anthony","Cena","jhon@gmail.com","password",Role.CLIENT);

        // save client in repository
        clientRepository.save(client);

        // Retrieve the client from the repository
        Client retrievedClient = clientRepository.findByEmail("jhon@gmail.com");

        // Verify tha the client retrieved is not null
        assertThat(retrievedClient).isNotNull();

        // verify the attributes of the recovered client
        assertThat(retrievedClient.getFirstName()).isEqualTo("John Felix Anthony");
        assertThat(retrievedClient.getLastName()).isEqualTo("Cena");
    }

    @Test
    public void findByEmailNoExistentEmailAndReturnsNull() {
        // search for a client by an email that does not exist
        //It is expected that if the email does not exist it returns null
        //the functionality of the search logic is ensured
        Client retrievedClient = clientRepository.findByEmail("false@mindhub.com");

        // Make sure the retrieved client is null
        //assertThat(retrievedClient).isNull();

        assertNull(retrievedClient, "A client was found with an email address that should not exist.");

    }

    //accountRepository test
    @Test
    public void whenFindByNumberThenReturnAccount() {
        //verifies that an Account object is correctly retrieved from the repository using the findByNumber query.
        // create a test account
        Account account = new Account("VIN-123456",LocalDate.now(),1000.0,TypeAccount.CURRENT_ACCOUNT,true);

        accountRepository.save(account);

        // when get an optional account
        Optional <Account> optionalAccount = accountRepository.findByNumber(account.getNumber());


        // Then
        assertThat(optionalAccount).isPresent();
        Account foundAccount = optionalAccount.get();
        assertThat(foundAccount.getNumber()).isEqualTo(account.getNumber());
    }

    @Test
    public void whenFindByClientThenReturnAccount(){
        // create an example client
        Client client = new Client("John Felix Anthony","Cena","jhon@gmail.com","password",Role.CLIENT);

        // create a test account
        Account account = new Account("VIN-123456",LocalDate.now(),1000.0,TypeAccount.CURRENT_ACCOUNT,true);

        //add account to client
        client.addAccount(account);

        // save client & account in repository
        accountRepository.save(account);
        clientRepository.save(client);

        //find account by client
        // Verify tha the account retrieved is not null
        assertThat(accountRepository.findByClient(client)).isNotNull();

        //Verify that the list of recovered accounts contains the created account
        List<Account> retrievedClientAccount = accountRepository.findByClient(client);
        assertThat(retrievedClientAccount).contains(account);

    }

    @Test
    public void testMapToAccountDTOList() {
        List <Account> accountlist= accountRepository.findAll();
        // Llamar al método que estamos probando
        List<AccountDTO> activeAccountDTOs = accountlist.stream()
                .filter(account -> account.isActive())
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());

        // Verificar que solo se obtengan las cuentas activas
        for (AccountDTO accountDTO : activeAccountDTOs) {
            assertTrue(accountDTO.isActive());
        }
    }



    //transaction repository test
    @Test
    public void testSaveTransactions() {

        // Create a fictitious transactions
        Transaction transaction1 = new Transaction(TransactionType.DEBIT, 500.0, "GOOGLE services", LocalDateTime.now().plusDays(1));
        Transaction transaction2 = new Transaction(TransactionType.DEBIT, 600.0, "GOOGLE services", LocalDateTime.now().plusDays(1));
        Transaction transaction3 = new Transaction(TransactionType.DEBIT, 800.0, "GOOGLE services", LocalDateTime.now().plusHours(5));

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);


        // Retrieve the transactions from the database
        List<Transaction> savedTransactions = transactionRepository.findAll();

        // verify that the recovered transactions match the dummy transactions
        assertTrue(savedTransactions.contains(transaction1));
        assertTrue(savedTransactions.contains(transaction2));
        assertTrue(savedTransactions.contains(transaction3));
    }
    @Test
    public void transactionUpdateTest(){

        // Create and save transactión
        Transaction transaction = new Transaction(TransactionType.DEBIT, 500.0, "Shopping", LocalDateTime.now());
        transactionRepository.save(transaction);

        // Modify the transaction
        transaction.setDescription("Supermarket");
        transactionRepository.save(transaction);

        // Retrieve the updated transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).orElse(null);

        assert updatedTransaction != null;

        assertEquals("Supermarket", updatedTransaction.getDescription());

    }

    @Test
    public void testFindByAccountAndAccountActiveIsTrue() {
        // create an active account
        Account activeAccount = new Account();
        activeAccount.setActive(true);
        activeAccount = accountRepository.save(activeAccount);

        // create an inactive account
        Account inactiveAccount = new Account();
        inactiveAccount.setActive(false);
        inactiveAccount = accountRepository.save(inactiveAccount);

        // Create some transactions related to both accounts
        Transaction testTransaction1 = new Transaction();
        testTransaction1.setAccount(activeAccount);
        testTransaction1 = transactionRepository.save(testTransaction1);

        Transaction testTransaction2 = new Transaction();
        testTransaction2.setAccount(activeAccount);
        testTransaction2 = transactionRepository.save(testTransaction2);

        Transaction testTransaction3 = new Transaction();
        testTransaction3.setAccount(inactiveAccount);
        testTransaction3 = transactionRepository.save(testTransaction3);

        // obtain lis transactions from active account
        List<Transaction> activeAccountTransactions = transactionRepository.findByAccountAndAccountActiveIsTrue(activeAccount);

        // Verify that only transactions from the active account are returned
        assertThat(activeAccountTransactions).containsExactlyInAnyOrder(testTransaction1,testTransaction2);
    }


    //card repository test

    @Test
    public void testExistsByNumber() {
        // create and save card
        Card card = new Card(CardType.DEBIT,CardColor.GOLD,"1234567890123456",LocalDate.now(), LocalDate.now().plusYears(2), 123, "John Cena");
        cardRepository.save(card);

        // verify that the card exist by number
        boolean exists = cardRepository.existsByNumber("1234567890123456");

        assertTrue(exists);
    }

    @Test
    public void testFindByNumber() {
        // create and save card
        Card card = new Card(CardType.DEBIT,CardColor.GOLD,"1234567890123456",LocalDate.now(), LocalDate.now().plusYears(2), 123, "John Cena");
        cardRepository.save(card);

        // search card by number
        Card foundCard = cardRepository.findByNumber("1234567890123456");

        assertNotNull(foundCard);

        assertEquals("John Cena", foundCard.getCardHolder());
    }






}
