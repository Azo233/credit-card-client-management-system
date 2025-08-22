package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class ClientAlreadyExistsException extends CreditCardManagementException {

    public ClientAlreadyExistsException(String oib) {
        super("Client with OIB '" + oib + "' already exists", "CLIENT_ALREADY_EXISTS");
    }

}
