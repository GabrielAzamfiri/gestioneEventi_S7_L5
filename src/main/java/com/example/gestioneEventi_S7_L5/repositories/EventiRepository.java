package com.example.gestioneEventi_S7_L5.repositories;

import com.example.gestioneEventi_S7_L5.entities.Evento;
import com.example.gestioneEventi_S7_L5.entities.Prenotazione;
import com.example.gestioneEventi_S7_L5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventiRepository extends JpaRepository<Evento, UUID> {

    List<Evento> findByOrganizzatore(Utente organizzatore);

}
