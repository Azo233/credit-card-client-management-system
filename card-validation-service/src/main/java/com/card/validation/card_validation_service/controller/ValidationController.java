package com.card.validation.card_validation_service.controller;

import com.card.validation.card_validation_service.dto.ClientValidationRequest;
import com.card.validation.card_validation_service.dto.ValidationResponse;
import com.card.validation.card_validation_service.service.ValidationService;
import com.card.validation.card_validation_service.validation.ValidationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/validation")
public class ValidationController {
    private static final Logger logger = LoggerFactory.getLogger(ValidationController.class);
    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/card-request")
    public ResponseEntity<ValidationResponse> validateCardRequest(
            @RequestBody ClientValidationRequest request) {

        logger.info("🔍 Validating request for: {} {}, OIB: {}",
                request.getFirstName(), request.getLastName(), request.getOib());

        ValidationResult result = validationService.validate(request);

        ValidationResponse response = new ValidationResponse(result.isValid(), result.getMessage());

        if (result.isValid()) {
            logger.info("Validation PASSED for OIB: {}", request.getOib());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Validation FAILED for OIB: {} - {}",
                    request.getOib(), result.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
