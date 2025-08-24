package com.card.validation.card_validation_service.events;

import java.time.LocalDateTime;

public class CardStatusEvent {
    private String requestId;
    private String oib;
    private String cardStatus;
    private String message;
    private LocalDateTime timestamp;

    public CardStatusEvent() {
    }

    public CardStatusEvent(String requestId, String oib, String cardStatus, String cardStatus1, String message, LocalDateTime timestamp) {
        this.requestId = requestId;
        this.oib = oib;
        this.cardStatus = cardStatus;
        this.cardStatus = cardStatus1;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
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
