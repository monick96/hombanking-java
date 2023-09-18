package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository <Transaction, Long>{

    List<Transaction> findByAccountIdAndDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByAccountAndAccountActiveIsTrue(Account account);
}
