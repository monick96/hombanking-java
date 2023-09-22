package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository <Loan,Long> {

    //Optional<Loan> findById(Long id);

}
