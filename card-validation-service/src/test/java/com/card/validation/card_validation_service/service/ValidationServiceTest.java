package com.card.validation.card_validation_service.service;


import com.card.validation.card_validation_service.dto.ClientValidationRequest;
import com.card.validation.card_validation_service.events.CardStatusEvent;
import com.card.validation.card_validation_service.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private ValidationService validationService;

    private ClientValidationRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new ClientValidationRequest();
        testRequest.setOib("12345678901");
        testRequest.setFirstName("Ivo");
        testRequest.setLastName("Ivić");
        testRequest.setStatus("PENDING");
    }
    @Test
    void validate() {
        ValidationResult result = validationService.validate(testRequest);

        assertTrue(result.isValid());
        assertEquals("New card request successfully created.", result.getMessage());
        assertNotNull(result.getRequestId());
        assertTrue(result.getRequestId().startsWith("REQ-"));

        ArgumentCaptor<CardStatusEvent> eventCaptor = ArgumentCaptor.forClass(CardStatusEvent.class);
        verify(kafkaTemplate).send(
                eq("card.status.update"),
                eq("12345678901"),
                eventCaptor.capture()
        );

        CardStatusEvent publishedEvent = eventCaptor.getValue();
        assertEquals("APPROVED", publishedEvent.getCardStatus());
        assertEquals("Card request approved", publishedEvent.getMessage());
        assertEquals("12345678901", publishedEvent.getOib());
        assertNotNull(publishedEvent.getRequestId());
        assertTrue(publishedEvent.getRequestId().startsWith("REQ-"));
    }
}
