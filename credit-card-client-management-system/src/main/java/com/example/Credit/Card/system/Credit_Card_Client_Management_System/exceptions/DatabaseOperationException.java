package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class DatabaseOperationException extends CreditCardManagementException {

    public DatabaseOperationException(String operation, Throwable cause) {
        super("Database operation failed: " + operation, "DATABASE_ERROR", cause);
    }
}
