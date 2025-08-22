package com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions;

public class ExternalApiException extends CreditCardManagementException {

    private final int httpStatus;

    public ExternalApiException(String message, int httpStatus) {
        super("External API error: " + message, "EXTERNAL_API_ERROR");
        this.httpStatus = httpStatus;
    }

    public ExternalApiException(String message, int httpStatus, Throwable cause) {
        super("External API error: " + message, "EXTERNAL_API_ERROR", cause);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
