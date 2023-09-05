package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByEmail(String email);

    Client findClientById(Long id);

//    Optional<Client> findById(Long id);

}
