package com.mindhub.Homebanking.services.implement;

import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.models.Role;
import com.mindhub.Homebanking.repositories.ClientRepository;
import com.mindhub.Homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository
                .findAll()
                .stream()
                .map(client -> this.getClientDTO(client))//o -> map(ClientDTO::new)
                .collect(toList());
    }

    @Override
    public ClientDTO getClientDTO(Client client) {
        return new ClientDTO(client);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findClientById(id);
    }

    @Override
    public List<Client> getClientsList() {
        return clientRepository.findAll();
    }

    @Override
    public Client createClient(String firstName, String lastName, String email, String password, Role role) {
        return new Client(firstName,lastName,email,password,role);
    }
}
