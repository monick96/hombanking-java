package com.mindhub.Homebanking;

import com.mindhub.Homebanking.models.*;
import com.mindhub.Homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	//password encryption
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args -> {
			//creating client instances
			Client client1 = new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("4lf4"));

			Client client2 = new Client("Javier","Miller","miller@mail.com", passwordEncoder.encode("m3g4"));

			//accounts
			//melba's first account
			Account account1 = new Account("VIN001",LocalDate.now(),5000.0);

			//melba's second account
			Account account2 = new Account("VIN002",LocalDate.now().plusDays(1),7500.0);

			//javier's account
			Account account3 = new Account("VIN003",LocalDate.now().plusDays(5),75000.0);

			//Transactions
			Transaction transaction1 = new Transaction(TransactionType.CREDIT,60000,"Fees", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,600,"Taxes", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,20000,"Donations", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,25000,"Payments", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.DEBIT,5000,"Shopping", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.CREDIT,15000,"Promotions", LocalDateTime.now());

			//Creating loan and payments
			List<Integer> payments1 = Arrays.asList(12, 24, 36, 48, 60);
			List<Integer> payments2 = Arrays.asList(6, 12, 24);
			List<Integer> payments3 = Arrays.asList(6, 12, 24, 36);

			Loan loan1 = new Loan("Mortgage",50000,payments1);
			Loan loan2 = new Loan("Personal",100000,payments2);
			Loan loan3 = new Loan("Automotive",300000,payments3);

			// Create ClientLoan for client Melba
			ClientLoan clientLoan1 = new ClientLoan(400000,60);
			ClientLoan clientLoan2 = new ClientLoan(500000,12);

			// Create ClientLoan for client Javier
			ClientLoan clientLoan3= new ClientLoan(100000,24);
			ClientLoan clientLoan4 = new ClientLoan(200000,36);

			//Saving clients
			clientRepository.save(client1);
			clientRepository.save(client2);

			//card object for Melba
			Card card1 = new Card(CardType.DEBIT,CardColor.GOLD,"8043275912834567",LocalDate.now(),LocalDate.now().plusYears(5), "123",client1.getFirstName() + " " + client1.getLastName());
			Card card2 = new Card(CardType.CREDIT,CardColor.TITANIUM,"8042275912834563",LocalDate.now(),LocalDate.now().plusYears(5), "321",client1.getFirstName() + " " + client1.getLastName());
			//card object for Javier
			Card card3 = new Card(CardType.CREDIT,CardColor.SILVER,"8042589683216345",LocalDate.now(),LocalDate.now().plusYears(5), "231",client2.getFirstName() + " " + client2.getLastName());

			// Saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//saving Loans
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			//saving ClientLoans
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			//addTransaction method - Establishing relationships account-transaction
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);

			//addAccount method - Establishing relationships account-client
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			//addClientLoan

			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			//addCard to client
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			//other way to use addClientLoan
			//client1.addClientLoan(new ClientLoan(400000, 60, client1, loan1));


			//saving transactions
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			//save accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//Saving clients
			clientRepository.save(client1);
			clientRepository.save(client2);

			//saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//saving ClientLoans
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			//saving Loans
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			//saving cards
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);



		});
	}

}
