package com.mindhub.Homebanking.services.implement;

import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.ClientLoan;
import com.mindhub.Homebanking.models.Loan;
import com.mindhub.Homebanking.repositories.ClientLoanRepository;
import com.mindhub.Homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public List<ClientLoan> getClientLoanByEmailAndLoanName(String email, String loanName) {
        return clientLoanRepository.findByClientEmailAndLoanName(email,loanName);
    }

    @Override
    public ClientLoan createClientLoan(Client client, Loan loan, int payments, double amount) {
        return new ClientLoan(client,loan,  payments, amount);
    }
}
