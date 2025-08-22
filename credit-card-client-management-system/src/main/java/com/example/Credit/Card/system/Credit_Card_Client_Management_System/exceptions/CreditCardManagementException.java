package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public abstract  class CreditCardManagementException extends RuntimeException{
    private final String errorCode;

    public CreditCardManagementException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CreditCardManagementException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
