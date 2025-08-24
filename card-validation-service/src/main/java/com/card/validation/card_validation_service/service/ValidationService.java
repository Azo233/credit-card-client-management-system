package com.card.validation.card_validation_service.service;

import com.card.validation.card_validation_service.dto.ClientValidationRequest;
import com.card.validation.card_validation_service.events.CardStatusEvent;
import com.card.validation.card_validation_service.validation.ValidationResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String CARD_STATUS_TOPIC = "card.status.update";

    public ValidationService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ValidationResult validate(ClientValidationRequest request) {

        String requestId = "REQ-" + System.currentTimeMillis();

        logger.info("Validating request for OIB: {}", request.getOib());

        if (!isValidOib(request.getOib())) {
            publishCardStatus(requestId, request.getOib(), "REJECTED", "Invalid OIB format");
            return new ValidationResult(false, "Invalid OIB format", requestId);
        }

        if (!isValidName(request.getFirstName(), request.getLastName())) {
            publishCardStatus(requestId, request.getOib(), "REJECTED", "Invalid name format");
            return new ValidationResult(false, "Invalid name format", requestId);
        }

        if (!isValidStatus(request.getStatus())) {
            publishCardStatus(requestId, request.getOib(), "REJECTED", "Invalid status");
            return new ValidationResult(false, "Invalid status", requestId);
        }

        publishCardStatus(requestId, request.getOib(), "APPROVED", "Card request approved");

        return new ValidationResult(true, "New card request successfully created.", requestId);
    }

    private void publishCardStatus(String requestId, String oib, String status, String message) {
        try {
            CardStatusEvent event = new CardStatusEvent();
            event.setCardStatus(status);
            event.setMessage(message);
            event.setOib(oib);
            event.setRequestId(requestId);

            kafkaTemplate.send(CARD_STATUS_TOPIC, oib, event);
            logger.info("Published card status '{}' for OIB: {}", status, oib);

        } catch (Exception e) {
            logger.error("Failed to publish card status for OIB: {} - {}", oib, e.getMessage());
        }
    }

    private boolean isValidOib(String oib) {
        return oib != null && oib.matches("\\d{11}");
    }

    private boolean isValidName(String firstName, String lastName) {
        return firstName != null && firstName.trim().length() >= 2 &&
                lastName != null && lastName.trim().length() >= 2;
    }

    private boolean isValidStatus(String status) {
        return !"REJECTED".equals(status) && !"CANCELLED".equals(status);
    }
}
