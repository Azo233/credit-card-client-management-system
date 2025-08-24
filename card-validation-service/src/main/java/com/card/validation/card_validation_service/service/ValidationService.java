package com.card.validation.card_validation_service.service;

import com.card.validation.card_validation_service.dto.ClientValidationRequest;
import com.card.validation.card_validation_service.events.CardStatusEvent;
import com.card.validation.card_validation_service.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;
    private boolean kafkaAvailable = true;

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
        if (kafkaTemplate == null || !kafkaAvailable) {
            logger.info("[KAFKA SIMULATION] Would publish to topic '{}': requestId={}, oib={}, status={}, message={}",
                    CARD_STATUS_TOPIC, requestId, oib, status, message);

            String eventJson = String.format(
                    "{\"requestId\":\"%s\",\"oib\":\"%s\",\"cardStatus\":\"%s\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                    requestId, oib, status, message, java.time.LocalDateTime.now()
            );
            logger.info("[EVENT SIMULATION] {}", eventJson);
            return;
        }

        try {
            kafkaTemplate.partitionsFor(CARD_STATUS_TOPIC);

            CardStatusEvent event = new CardStatusEvent();
            event.setCardStatus(status);
            event.setMessage(message);
            event.setOib(oib);
            event.setRequestId(requestId);
            event.setTimestamp(java.time.LocalDateTime.now());

            kafkaTemplate.send(CARD_STATUS_TOPIC, oib, event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            logger.info("Successfully published card status '{}' for OIB: {}", status, oib);
                        } else {
                            logger.error("Failed to publish card status: {}", ex.getMessage());
                            kafkaAvailable = false;
                        }
                    });

        } catch (Exception e) {
            logger.warn("Kafka not available, falling back to simulation mode: {}", e.getMessage());
            kafkaAvailable = false;

            logger.info("[KAFKA FALLBACK] Simulating publish: topic={}, key={}, status={}, message={}",
                    CARD_STATUS_TOPIC, oib, status, message);
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
