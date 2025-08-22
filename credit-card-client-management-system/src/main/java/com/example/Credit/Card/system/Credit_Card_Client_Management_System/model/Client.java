package com.example.Credit.Card.system.Credit_Card_Client_Management_System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "OIB is required")
    @Pattern(regexp = "\\d{11}", message = "OIB must be exactly 11 digits")
    @Column(unique = true, nullable = false, length = 11)
    private String oib;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    public Client() {}

    public Client(String firstName, String lastName, String oib, CardStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.oib = oib;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", oib='" + oib + '\'' +
                ", status=" + status +
                '}';
    }
}


