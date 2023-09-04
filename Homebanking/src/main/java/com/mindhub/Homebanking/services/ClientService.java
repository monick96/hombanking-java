package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    //abstract methods
    void saveClient(Client client);//91

    List<ClientDTO> getClientsDTO();//38

    ClientDTO getClientDTO(Client client);

    Client getClientFindByEmail(String email);//linea 82/132/173

    Optional<Client> getClientFindById(Long id);//135

    List<Client> getClientsList();//linea 106

}
