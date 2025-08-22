package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class InvalidCardStatusException extends CreditCardManagementException {

    public InvalidCardStatusException(String status) {
        super("Invalid card status: '" + status + "'", "INVALID_CARD_STATUS");
    }
}
