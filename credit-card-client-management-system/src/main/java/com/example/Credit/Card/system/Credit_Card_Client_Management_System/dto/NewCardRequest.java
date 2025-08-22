package com.example.Credit.Card.system.Credit_Card_Client_Management_System.dto;

public class NewCardRequest {
    private String firstName;
    private String lastName;
    private String status;
    private String oib;

    public NewCardRequest() {}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    @Override
    public String toString() {
        return "NewCardRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", oib='" + oib + '\'' +
                '}';
    }
}