package com.example.Credit.Card.system.Credit_Card_Client_Management_System.controller;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ClientRequest;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.services.ClientService;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.services.ExternalApiService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;
    private final ExternalApiService externalApiService;

    public ClientController(ClientService clientService, ExternalApiService externalApiService) {
        this.clientService = clientService;
        this.externalApiService = externalApiService;
    }

    @GetMapping(name = "/getAll")
    public ResponseEntity<Page<Client>> getAllClients(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<Client> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/getByOib/{oib}")
    public ResponseEntity<Client> getClientByOib(@PathVariable String oib) {
        Client client = clientService.getByOib(oib);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@Valid @RequestBody ClientRequest request) {
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setOib(request.getOib());
        client.setStatus(request.getStatus());

        Client createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @PutMapping("/update")
    private ResponseEntity<Client> updateClient (@Valid @RequestBody ClientRequest request){
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setOib(request.getOib());
        client.setStatus(request.getStatus());
        Client updateClient = clientService.updateClinet(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(updateClient);
    }

    @PostMapping("/external")
    public ResponseEntity<String> submitToExternalApi(@Valid @RequestBody Client client) {
        externalApiService.submitCardRequest(client);
        return ResponseEntity.ok("Data successfully submitted to external API");
    }

    @DeleteMapping("/delete/{oib}")
    public ResponseEntity<Void> deleteClient(@PathVariable String oib) {
        clientService.deleteClientByOib(oib);
        return ResponseEntity.noContent().build();
    }

}
