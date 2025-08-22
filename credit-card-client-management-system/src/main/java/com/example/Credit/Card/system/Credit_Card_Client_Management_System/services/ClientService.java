package com.example.Credit.Card.system.Credit_Card_Client_Management_System.services;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions.ClientAlreadyExistsException;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions.ClientNotFoundException;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions.DatabaseOperationException;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    public Page<Client> getAllClients(Pageable pageable) {return clientRepository.findAll(pageable);}

    public Client getByOib(String oib){
        return (clientRepository.findByOib(oib)
                .orElseThrow(() -> new ClientNotFoundException(oib)));
    }
    public Client createClient(Client client) {
        if (clientRepository.existsByOib(client.getOib())) {
            throw new ClientAlreadyExistsException(client.getOib());
        }

        try {
            return clientRepository.save(client);
        } catch (Exception e) {
            throw new DatabaseOperationException("create client", e);
        }
    }

    public void deleteClientByOib(String oib) {
        Client client = clientRepository.findByOib(oib)
                .orElseThrow(() -> new ClientNotFoundException(oib));

        try {
            clientRepository.delete(client);
        } catch (Exception e) {
            throw new DatabaseOperationException("delete client", e);
        }
    }

    public Client updateClinet (Client client){
        Client existingClient  = clientRepository.findByOib(client.getOib())
                .orElseThrow(() -> new ClientNotFoundException(client.getOib()));

        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setStatus(client.getStatus());
        try {
            return clientRepository.save(existingClient);
        }catch (Exception e){
            throw new DatabaseOperationException("update client", e);
        }
    }
}

