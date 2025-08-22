package com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto;

import java.time.LocalDateTime;

public class ValidationResponse {
    private String message;
    private LocalDateTime timestamp;

    public ValidationResponse() {
    }

    public ValidationResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
