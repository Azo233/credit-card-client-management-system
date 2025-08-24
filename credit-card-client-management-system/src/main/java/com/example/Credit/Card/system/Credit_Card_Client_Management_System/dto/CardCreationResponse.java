package com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto;

public class CardCreationResponse {
    private String message;
    private String requestId;
    private String status;

    public CardCreationResponse() {
    }

    public CardCreationResponse(String message, String requestId, String status) {
        this.message = message;
        this.requestId = requestId;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
