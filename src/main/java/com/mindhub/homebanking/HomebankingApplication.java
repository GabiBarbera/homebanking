package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {
	LocalDateTime date = LocalDateTime.now();
	LocalDateTime date1 = LocalDateTime.now().plusDays(1);

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository accountRepository) {
		return (args) -> {
			Account account1 = new Account("VIN001",this.date, 5000);
			Account account2 = new Account("VIN002",this.date1,7500);
			Client client = new Client("Gabriel", "Barbera","gabriel.barberaa@gmail.com");
			client.addAccount(account1);
			client.addAccount(account2);
			repositoryClient.save(client);
			accountRepository.save(account1);
			accountRepository.save(account2);
		};
	}
}


