package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class InvalidOibException extends CreditCardManagementException {

    public InvalidOibException(String oib) {
        super("Invalid OIB format: '" + oib + "'. OIB must be exactly 11 digits", "INVALID_OIB");
    }

    public InvalidOibException(String oib, String reason) {
        super("Invalid OIB '" + oib + "': " + reason, "INVALID_OIB");
    }
}
