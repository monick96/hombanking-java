package com.mindhub.Homebanking;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.AccountRepository;
import com.mindhub.Homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com");

			Client client2 = new Client("Javier","Miller","miller@mail.com");

			//accounts
			//melba's first account
			Account account1 = new Account("VIN001",LocalDate.now(),5000.0);

			//melba's second account
			Account account2 = new Account("VIN002",LocalDate.now().plusDays(1),7500.0);

			//javier's account
			Account account3 = new Account("VIN003",LocalDate.now().plusDays(5),75000.0);


			//addAccount method - Establishing relationships
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			//Saving clients
			clientRepository.save(client1);
			clientRepository.save(client2);


			// Saving accounts
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

		});
	}

}
