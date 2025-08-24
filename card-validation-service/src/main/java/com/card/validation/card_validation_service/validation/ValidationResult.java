package com.card.validation.card_validation_service.validation;

public class ValidationResult {
    private  boolean valid;
    private  String message;
    private  String requestId;

    public ValidationResult() {
    }

    public ValidationResult(boolean valid, String message, String requestId) {
        this.valid = valid;
        this.message = message;
        this.requestId = requestId;
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
}

