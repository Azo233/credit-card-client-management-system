package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class ClientOperationNotAllowedException extends CreditCardManagementException {

    public ClientOperationNotAllowedException(String oib, String operation, String reason) {
        super("Operation '" + operation + "' not allowed for client " + oib + ": " + reason,
                "OPERATION_NOT_ALLOWED");
    }
}
