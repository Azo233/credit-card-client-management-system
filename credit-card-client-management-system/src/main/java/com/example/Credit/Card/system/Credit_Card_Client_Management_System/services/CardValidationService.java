package com.example.Credit.Card.system.Credit_Card_Client_Management_System.services;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ClientRequest;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ErrorResponse;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.NewCardRequest;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ValidationResponse;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class CardValidationService {

    private static final Logger logger = LoggerFactory.getLogger(CardValidationService.class);

    private final RestTemplate restTemplate;

    @Value("${external.card-validation-service.url}")
    private String validationServiceUrl;

    public CardValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> cardValidation(ClientRequest request) {
        logger.info("Submitting card request to card validation for request: {} {}, OIB: {}",
                request.getFirstName(), request.getLastName(), request.getOib());

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");
            headers.set("User-Agent", "Credit-Card-Management-8081");

            HttpEntity<ClientRequest> httpEntity = new HttpEntity<>(request, headers);

            String fullUrl = validationServiceUrl + "/api/v1/card-request";
            logger.debug("Sending request to external API: {}", fullUrl);

            ResponseEntity<ValidationResponse> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.POST,
                    httpEntity,
                    ValidationResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
            } else {
                ErrorResponse error = new ErrorResponse(
                        "EXTERNAL_API_ERROR",
                        UUID.randomUUID().toString(),
                        "External service returned status: " + response.getStatusCode()
                );
                return ResponseEntity.status(response.getStatusCode()).body(error);
            }

        } catch (HttpClientErrorException e) {
            logger.error("Client error from external API for OIB {}: {} - Response: {}",
                    request.getOib(), e.getMessage(), e.getResponseBodyAsString());

            String errorMessage = extractErrorMessage(e.getResponseBodyAsString());

            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                ErrorResponse error = new ErrorResponse(
                        "BAD_REQUEST",
                        UUID.randomUUID().toString(),
                        errorMessage
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                ErrorResponse error = new ErrorResponse(
                        "UNAUTHORIZED",
                        UUID.randomUUID().toString(),
                        "Unauthorized access to external service"
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                ErrorResponse error = new ErrorResponse(
                        "NOT_FOUND",
                        UUID.randomUUID().toString(),
                        "External service endpoint not found"
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

            } else {
                ErrorResponse error = new ErrorResponse(
                        "BAD_REQUEST",
                        UUID.randomUUID().toString(),
                        errorMessage
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

        } catch (HttpServerErrorException e) {
            logger.error("Server error from external API for OIB {}: {}",
                    request.getOib(), e.getMessage());

            ErrorResponse error = new ErrorResponse(
                    "INTERNAL_SERVER_ERROR",
                    UUID.randomUUID().toString(),
                    "Internal server error occurred"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        } catch (ResourceAccessException e) {
            logger.error("Connection error to external API for OIB {}: {}",
                    request.getOib(), e.getMessage());

            ErrorResponse error = new ErrorResponse(
                    "INTERNAL_SERVER_ERROR",
                    UUID.randomUUID().toString(),
                    "Internal server error occurred"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        } catch (Exception e) {
            logger.error("Unexpected error when calling external API for OIB {}: {}",
                    request.getOib(), e.getMessage(), e);

            ErrorResponse error = new ErrorResponse(
                    "INTERNAL_SERVER_ERROR",
                    UUID.randomUUID().toString(),
                    "Internal server error occurred"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private String extractErrorMessage(String responseBody) {
        try {
            if (responseBody.contains("\"message\":\"")) {
                int start = responseBody.indexOf("\"message\":\"") + 11;
                int end = responseBody.indexOf("\"", start);
                if (end > start) {
                    return responseBody.substring(start, end);
                }
            }

            return "Validation failed";
        } catch (Exception ex) {
            logger.warn("Could not parse error message from external API response: {}", ex.getMessage());
            return "Invalid request";
        }
    }


    private NewCardRequest mapClientToNewCardRequest(Client client) {
        NewCardRequest request = new NewCardRequest();
        request.setFirstName(client.getFirstName());
        request.setLastName(client.getLastName());
        request.setOib(client.getOib());
        request.setStatus(client.getStatus().toString());

        return request;
    }
}




