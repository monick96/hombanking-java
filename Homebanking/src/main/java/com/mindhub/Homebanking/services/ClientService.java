package com.mindhub.Homebanking.services;

import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Client;

import java.util.List;

public interface ClientService {

    void saveClient(Client client);

    List<ClientDTO> getClientsDTO();




}
