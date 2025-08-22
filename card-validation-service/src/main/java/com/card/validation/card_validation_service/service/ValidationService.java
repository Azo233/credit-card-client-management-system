package com.card.validation.card_validation_service.service;

import com.card.validation.card_validation_service.dto.ClientValidationRequest;
import com.card.validation.card_validation_service.validation.ValidationResult;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public ValidationResult validate(ClientValidationRequest request) {

        if (!isValidOib(request.getOib())) {
            return new ValidationResult(false, "Invalid OIB format");
        }
        if (!isValidName(request.getFirstName(), request.getLastName())) {
            return new ValidationResult(false, "Invalid name format");
        }
        if (!isValidStatus(request.getStatus())) {
            return new ValidationResult(false, "Invalid status");
        }
        return new ValidationResult(true, "Validation passed");
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
