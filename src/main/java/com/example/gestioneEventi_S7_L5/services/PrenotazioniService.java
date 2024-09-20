package com.example.gestioneEventi_S7_L5.services;

import com.example.gestioneEventi_S7_L5.entities.Evento;
import com.example.gestioneEventi_S7_L5.entities.Prenotazione;
import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.exceptions.BadRequestException;
import com.example.gestioneEventi_S7_L5.exceptions.NotFoundException;
import com.example.gestioneEventi_S7_L5.payloads.PrenotazioneDTO;
import com.example.gestioneEventi_S7_L5.repositories.EventiRepository;
import com.example.gestioneEventi_S7_L5.repositories.PrenotazioniRepository;
import com.example.gestioneEventi_S7_L5.repositories.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioniService {

    @Autowired
    private PrenotazioniRepository prenotazioniRepository;

    @Autowired
    private UtentiRepository utentiRepository;

    @Autowired
    private EventiRepository eventiRepository;


    public Page<Prenotazione> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioniRepository.findAll(pageable);
    }


    public List<Prenotazione> findByUtente( Utente utente){
        return prenotazioniRepository.findByUtente(utente);
    }

    public Prenotazione save(PrenotazioneDTO prenotazioneDTO){
        UUID uuidUtente;
        UUID uuidEvento;
        try {
            uuidUtente=  UUID.fromString(prenotazioneDTO.utente());
            uuidEvento=  UUID.fromString(prenotazioneDTO.evento());
        }catch (Exception e){
            throw new BadRequestException("L'id inserito non è valido! Necessario inserire un ID di Tipo UUID");
        }


        Utente utente = utentiRepository.findById(uuidUtente).orElseThrow(() ->  new NotFoundException(uuidUtente));
        Evento evento = eventiRepository.findById(uuidEvento).orElseThrow(() ->  new NotFoundException(uuidEvento));

        if (evento.getNrPostiDisponibili() > evento.getPrenotazioneList().size()) {
            Prenotazione prenotazione = new Prenotazione(utente,evento);
            return this.prenotazioniRepository.save(prenotazione);

        }else {
            throw new BadRequestException("Non ci sono più posti a disposizione per questo Evento!!");
        }
    }

    public Prenotazione findById(UUID prenotazioneId){
        return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(UUID  prenotazioneId, PrenotazioneDTO updatedPrenotazioneDTO){
        Prenotazione found = findById(prenotazioneId);
        UUID uuidUtente;
        UUID uuidEvento;
        try {
            uuidUtente=  UUID.fromString(updatedPrenotazioneDTO.utente());
            uuidEvento=  UUID.fromString(updatedPrenotazioneDTO.evento());
        }catch (Exception e){
            throw new BadRequestException("L'id inserito non è valido! Necessario inserire un ID di Tipo UUID");
        }


        Utente utente = utentiRepository.findById(uuidUtente).orElseThrow(() ->  new NotFoundException(uuidUtente));
        Evento evento = eventiRepository.findById(uuidEvento).orElseThrow(() ->  new NotFoundException(uuidEvento));


        found.setEvento(evento);
        found.setUtente(utente);

        return this.prenotazioniRepository.save(found);
    }

    public void findByIdAndDelete(UUID  prenotazioneId){
        Prenotazione found = findById(prenotazioneId);
        this.prenotazioniRepository.delete(found);
    }
}
