package com.example.gestioneEventi_S7_L5.repositories;

import com.example.gestioneEventi_S7_L5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtentiRepository extends JpaRepository<Utente, UUID> {

    Optional<Utente> findByEmail(String email);
}
