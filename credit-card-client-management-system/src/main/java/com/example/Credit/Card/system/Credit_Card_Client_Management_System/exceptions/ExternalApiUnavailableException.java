package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class ExternalApiUnavailableException extends CreditCardManagementException {

    public ExternalApiUnavailableException(String apiUrl) {
        super("External API unavailable: " + apiUrl, "EXTERNAL_API_UNAVAILABLE");
    }

    public ExternalApiUnavailableException(String apiUrl, Throwable cause) {
        super("External API unavailable: " + apiUrl, "EXTERNAL_API_UNAVAILABLE", cause);
    }
}
