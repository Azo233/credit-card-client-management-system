package com.example.Credit.Card.system.Credit_Card_Client_Management_System.services;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ClientRequest;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardValidationServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CardValidationService cardValidationService;

    private ClientRequest testRequest;
    private ValidationResponse testValidationResponse;
    private String validationServiceUrl;

    @BeforeEach
    void setUp() {
        testRequest = new ClientRequest();
        testRequest.setOib("12345678901");
        testRequest.setFirstName("Ivo");
        testRequest.setLastName("Ivić");

        testValidationResponse = new ValidationResponse();
        testValidationResponse.setMessage("Validation successful");

        validationServiceUrl = "http://localhost:8082";

        ReflectionTestUtils.setField(cardValidationService, "validationServiceUrl", validationServiceUrl);
    }

    @Test
    void cardValidation() {
        ResponseEntity<ValidationResponse> mockResponse =
                ResponseEntity.status(HttpStatus.OK).body(testValidationResponse);

        when(restTemplate.exchange(
                eq(validationServiceUrl + "/api/v1/card-request"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ValidationResponse.class)
        )).thenReturn(mockResponse);

        ResponseEntity<?> result = cardValidationService.cardValidation(testRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(testValidationResponse, result.getBody());

        verify(restTemplate).exchange(
                eq(validationServiceUrl + "/api/v1/card-request"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ValidationResponse.class)
        );
    }
}