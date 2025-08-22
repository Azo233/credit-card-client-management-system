package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class ClientNotFoundException extends CreditCardManagementException{

    public ClientNotFoundException(String oib) {
        super("Client with OIB '" + oib + "' not found", "CLIENT_NOT_FOUND");
    }

    public ClientNotFoundException(Long id) {
        super("Client with ID " + id + " not found", "CLIENT_NOT_FOUND");
    }
}
