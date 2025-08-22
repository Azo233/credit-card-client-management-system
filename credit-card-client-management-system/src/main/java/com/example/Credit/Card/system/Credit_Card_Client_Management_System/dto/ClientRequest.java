package com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.CardStatus;

public class ClientRequest {

    private String firstName;
    private String lastName;
    private String oib;
    private CardStatus status;

    public ClientRequest() {
    }

    public ClientRequest(String firstName, String lastName, String oib, CardStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.oib = oib;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }
}


