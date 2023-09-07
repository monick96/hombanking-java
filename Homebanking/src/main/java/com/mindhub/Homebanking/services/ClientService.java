package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.Role;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    //abstract methods
    void saveClient(Client client);

    List<ClientDTO> getClientsDTO();

    ClientDTO getClientDTO(Client client);

    Client getClientByEmail(String email);

    Client getClientById(Long id);

    List<Client> getClientsList();

    Client createClient(String firstName, String lastName, String email, String password, Role role);

}
