package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository <ClientLoan, Long>{

    //search for loans by customer name and loan name
    List<ClientLoan> findByClientEmailAndLoanName(String clientEmail, String loanName);

}
