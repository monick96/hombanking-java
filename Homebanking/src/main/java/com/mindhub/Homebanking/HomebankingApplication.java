package com.mindhub.Homebanking;

import com.mindhub.Homebanking.models.*;
import com.mindhub.Homebanking.repositories.AccountRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.repositories.LoanRepository;
import com.mindhub.Homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository){
		return (args -> {
			//creating client instances
			Client client1 = new Client("Melba","Morel","melba@mindhub.com");

			Client client2 = new Client("Javier","Miller","miller@mail.com");

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
			Set<Integer> payments1 = new HashSet<>(Arrays.asList(12,24,36,48,60));
			Set<Integer> payments2 = new HashSet<>(Arrays.asList(6, 12, 24));
			Set<Integer> payments3 = new HashSet<>(Arrays.asList(6, 12, 24, 36));

			Loan loan1 = new Loan("Mortgage",500000,payments1);
			Loan loan2 = new Loan("Personal",100000,payments2);
			Loan loan3 = new Loan("Automotive",300000,payments3);



			//Saving clients
			clientRepository.save(client1);
			clientRepository.save(client2);

			// Saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//saving Loans
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			//addTransaction method - Establishing relationships account-transaction

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);

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


			//addAccount method - Establishing relationships account-client
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);


			//Saving clients
			clientRepository.save(client1);
			clientRepository.save(client2);

			//saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);



		});
	}

}
