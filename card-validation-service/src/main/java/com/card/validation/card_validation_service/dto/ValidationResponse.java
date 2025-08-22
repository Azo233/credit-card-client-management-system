package com.card.validation.card_validation_service.dto;

import java.time.LocalDateTime;

public class ValidationResponse {
    private boolean valid;
    private String message;
    private String requestId;
    private LocalDateTime timestamp;

    public ValidationResponse() {}

    public ValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
        this.requestId = "VAL-" + System.currentTimeMillis();
        this.timestamp = LocalDateTime.now();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
