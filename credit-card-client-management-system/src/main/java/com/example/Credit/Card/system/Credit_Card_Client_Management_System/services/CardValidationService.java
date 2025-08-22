package com.example.Credit.Card.system.Credit_Card_Client_Management_System.services;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.NewCardRequest;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.ValidationResponse;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions.ExternalApiException;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.exceptions.ExternalApiUnavailableException;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestTemplate restTemplate;

    @Value("${app.validation-service.url:http://localhost:8082/api/v1/validation/card-request}")
    private String validationServiceUrl;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ValidationResponse cardValidation(Client client) {
        logger.info("Submitting card request to card validation for client: {} {}, OIB: {}",
                client.getFirstName(), client.getLastName(), client.getOib());
        try {
            NewCardRequest request = mapClientToNewCardRequest(client);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Accept", "application/json");
            headers.set("User-Agent", "Credit-Card-Management-8081");

            HttpEntity<NewCardRequest> httpEntity = new HttpEntity<>(request, headers);

            logger.debug("Sending request to external API: {}", validationServiceUrl);

            ResponseEntity<ValidationResponse> response = restTemplate.exchange(
                    validationServiceUrl,
                    HttpMethod.POST,
                    httpEntity,
                    ValidationResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.debug("External API response: {}", response.getBody());
                return response.getBody();
            } else {
                logger.warn("Unexpected response status from external API: {} for OIB: {}",
                        response.getStatusCode(), client.getOib());
                throw new ExternalApiException(
                        "Unexpected response status: " + response.getStatusCode(),
                        response.getStatusCode().value()
                );
            }

        } catch (HttpClientErrorException e) {
            logger.error("Client error from external API for OIB {}: {} - Response: {}",
                    client.getOib(), e.getMessage(), e.getResponseBodyAsString());
            throw new ExternalApiException(
                    "External API client error: " + e.getMessage(),
                    e.getStatusCode().value(),
                    e
            );

        } catch (HttpServerErrorException e) {
            logger.error("Server error from external API for OIB {}: {}",
                    client.getOib(), e.getMessage());
            throw new ExternalApiException(
                    "External API server error: " + e.getMessage(),
                    e.getStatusCode().value(),
                    e
            );

        } catch (ResourceAccessException e) {
            logger.error("Connection error to external API for OIB {}: {}",
                    client.getOib(), e.getMessage());
            throw new ExternalApiUnavailableException(validationServiceUrl, e);

        } catch (Exception e) {
            logger.error("Unexpected error when calling external API for OIB {}: {}",
                    client.getOib(), e.getMessage(), e);
            throw new ExternalApiException("Unexpected error: " + e.getMessage(), 500, e);
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




