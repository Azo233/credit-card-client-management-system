package com.example.Credit.Card.system.Credit_Card_Client_Management_System.services;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.CardStatus;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private Pageable pageable;

    @BeforeEach
    void setUp(){
        testClient = new Client();
        testClient.setOib("12345678901");
        testClient.setFirstName("Ivo");
        testClient.setLastName("Ivić");
        testClient.setStatus(CardStatus.APPROVED);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getAllClients() {
        Page<Client> expectedPage = new PageImpl<>(Arrays.asList(testClient));
        when(clientRepository.findAll(pageable)).thenReturn(expectedPage);
        Page<Client> result = clientService.getAllClients(pageable);
        assertEquals(expectedPage, result);
        assertEquals(1, result.getTotalElements());
        verify(clientRepository).findAll(pageable);
    }

    @Test
    void getByOib() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(testClient));
        Client result = clientService.getByOib("12345678901");
        assertEquals(testClient, result);
        verify(clientRepository).findByOib("12345678901");
    }

    @Test
    void createClient() {
        when(clientRepository.existsByOib("12345678901")).thenReturn(false);
        when(clientRepository.save(testClient)).thenReturn(testClient);
        Client result = clientService.createClient(testClient);
        assertEquals(testClient, result);
        verify(clientRepository).existsByOib("12345678901");
        verify(clientRepository).save(testClient);
    }

    @Test
    void deleteClientByOib() {
        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(testClient));
        assertDoesNotThrow(() -> clientService.deleteClientByOib("12345678901"));
        verify(clientRepository).findByOib("12345678901");
        verify(clientRepository).delete(testClient);
    }

    @Test
    void updateClinet() {
        Client existingClient = new Client();
        existingClient.setOib("12345678901");
        existingClient.setFirstName("Ivo");
        existingClient.setLastName("Ivic");
        existingClient.setStatus(CardStatus.PENDING);

        Client updatedClient = new Client();
        updatedClient.setOib("12345678901");
        updatedClient.setFirstName("Pero");
        updatedClient.setLastName("Perić");
        updatedClient.setStatus(CardStatus.APPROVED);

        when(clientRepository.findByOib("12345678901")).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client result = clientService.updateClinet(updatedClient);

        assertEquals("Pero", result.getFirstName());
        assertEquals("Perić", result.getLastName());
        assertEquals(CardStatus.APPROVED, result.getStatus());
        verify(clientRepository).findByOib("12345678901");
        verify(clientRepository).save(existingClient);
    }
}