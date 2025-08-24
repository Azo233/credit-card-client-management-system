package com.card.validation.card_validation_service.dto;

public class ErrorResponse {
    private String code;

    private String id;

    private String description;

    public ErrorResponse() {}

    public ErrorResponse(String code, String id, String description) {
        this.code = code;
        this.id = id;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
