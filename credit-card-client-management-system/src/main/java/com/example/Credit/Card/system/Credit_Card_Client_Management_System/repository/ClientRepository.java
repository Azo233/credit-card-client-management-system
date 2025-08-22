package com.example.Credit.Card.system.Credit_Card_Client_Management_System.repository;

import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.CardStatus;
import com.example.Credit.Card.system.Credit_Card_Client_Management_System.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends  JpaRepository<Client,Long> {

    Optional<Client> findByOib(String oib);

    boolean existsByOib(String oib);
    
    List<Client> findByStatus(CardStatus status);

    Page<Client> findByStatus(CardStatus status, Pageable pageable);
}
