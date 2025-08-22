package com.example.Credit.Card.system.Credit_Card_Client_Management_System.services;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto.NewCardRequest;
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

        @Value("${app.external-api.url:https://api.creditCardSystem.com/v1/api/v1/card-request}")
        private String externalApiUrl;

        public ExternalApiService(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        public void submitCardRequest(Client client) {
            logger.info("Submitting card request for client with OIB: {}", client.getOib());

            try {
                NewCardRequest request = mapClientToNewCardRequest(client);

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                headers.set("Accept", "application/json");

                HttpEntity<NewCardRequest> httpEntity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        externalApiUrl,
                        HttpMethod.POST,
                        httpEntity,
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Successfully submitted card request for OIB: {}", client.getOib());
                } else {
                    logger.warn("Unexpected response status: {} for OIB: {}",
                            response.getStatusCode(), client.getOib());
                    throw new ExternalApiException(
                            "Unexpected response status: " + response.getStatusCode(),
                            response.getStatusCode().value()
                    );
                }

            } catch (HttpClientErrorException e) {
                logger.error("Client error when submitting card request for OIB {}: {}",
                        client.getOib(), e.getMessage());
                throw new ExternalApiException(
                        "Client error: " + e.getMessage(),
                        e.getStatusCode().value(),
                        e
                );

            } catch (HttpServerErrorException e) {
                logger.error("Server error when submitting card request for OIB {}: {}",
                        client.getOib(), e.getMessage());
                throw new ExternalApiException(
                        "Server error: " + e.getMessage(),
                        e.getStatusCode().value(),
                        e
                );

            } catch (ResourceAccessException e) {
                logger.error("Connection error when submitting card request for OIB {}: {}",
                        client.getOib(), e.getMessage());
                throw new ExternalApiUnavailableException(externalApiUrl, e);

            } catch (Exception e) {
                logger.error("Unexpected error when submitting card request for OIB {}: {}",
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

