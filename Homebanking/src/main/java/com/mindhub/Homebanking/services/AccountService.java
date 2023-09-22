package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.dtos.AccountDTO;
import com.mindhub.Homebanking.models.Account;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.TypeAccount;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    void saveAccount(Account account);

    Optional<Account> getOptionalAccountByNumber(String number);

    List<AccountDTO> getListAccountDTO();

    Optional<Account> getOptionalAccountById(Long id);

    List<Account> getAccountsByClient(Client client);

    List<AccountDTO> mapToAccountDTOList(List<Account> accounts);

    AccountDTO getAccountDTO(Account account);

    Account createAccount(String number, LocalDate creationDate, double balance, TypeAccount typeAccount,boolean active);

    void deactivateAccount(Account account,boolean active);

}
