package com.example.Credit.Card.system.Credit_Card_Client_Management_System.repository;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends  JpaRepository<Client,Long> {

}
