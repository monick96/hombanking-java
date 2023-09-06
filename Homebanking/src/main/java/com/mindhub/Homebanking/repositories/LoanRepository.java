package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository <Loan,Long> {

    Optional<Loan> findById(Long id);

}
