package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.ClientLoan;
import com.mindhub.Homebanking.models.Loan;

import java.util.List;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);

    List<ClientLoan> getClientLoanByEmailAndLoanName(String email, String loanName);

    ClientLoan createClientLoan( Client client, Loan loan, int payments, double amount);


}
