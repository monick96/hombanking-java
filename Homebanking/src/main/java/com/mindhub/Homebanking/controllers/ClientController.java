package com.mindhub.Homebanking.controllers;

import com.mindhub.Homebanking.dtos.ClientDTO;
import com.mindhub.Homebanking.models.Client;
import com.mindhub.Homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;


    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository
                .findAll()
                .stream()
                .map(ClientDTO::new)//o -> map(client -> new ClientDTO(client))
                .collect(toList());

    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ClientDTO::new)
                .orElse(null);
    }
}
