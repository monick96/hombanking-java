package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClient(Client client);

    Optional<Account> findByNumber(String number);



}
