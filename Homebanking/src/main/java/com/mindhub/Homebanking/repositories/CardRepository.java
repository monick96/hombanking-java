package com.mindhub.Homebanking.repositories;

import com.mindhub.Homebanking.models.Card;
import com.mindhub.Homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);

    boolean existsByCvv(int cvv);

    Card findByNumber(String number);
}
