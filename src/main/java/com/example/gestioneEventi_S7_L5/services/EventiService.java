package com.example.gestioneEventi_S7_L5.services;

import com.example.gestioneEventi_S7_L5.entities.Evento;
import com.example.gestioneEventi_S7_L5.entities.Prenotazione;
import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.exceptions.BadRequestException;
import com.example.gestioneEventi_S7_L5.exceptions.NotFoundException;
import com.example.gestioneEventi_S7_L5.payloads.EventoDTO;
import com.example.gestioneEventi_S7_L5.repositories.EventiRepository;
import com.example.gestioneEventi_S7_L5.repositories.UtentiRepository;
import com.example.gestioneEventi_S7_L5.security.JWTCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EventiService {
    @Autowired
    private EventiRepository eventiRepository;

    @Autowired
    private  UtentiRepository utentiRepository;

    @Autowired
    private JWTCheckFilter jwtCheckFilter;

    public Page<Evento> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventiRepository.findAll(pageable);
    }

    public Evento save(EventoDTO eventoDTO){

        UUID uuidUtente;
        LocalDate dataEvento = null;
        try {
            dataEvento= LocalDate.parse(eventoDTO.dataEvento());
        }catch (Exception e){
            throw new BadRequestException("Errore: il formato data inserito non è corretto. inserire un formato data (yyyy-mm-dd)");
        }

//        try {
//            uuidUtente=  UUID.fromString(eventoDTO.organizzatore());
//        }catch (Exception e){
//            throw new BadRequestException("L'id inserito non è valido! Necessario inserire un ID di Tipo UUID");
//        }
//        Utente organizzatore = utentiRepository.findById(uuidUtente).orElseThrow(() ->  new NotFoundException(uuidUtente));

        //tramite l'authentication riesco ad accedere al utente organizzatore del evento e passarlo come organizzatore alla creazione del evento
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Utente organizzatore = (Utente) authentication.getPrincipal();

        Evento evento = new Evento(eventoDTO.titolo(),eventoDTO.descrizione(),dataEvento,eventoDTO.luogo() ,eventoDTO.nrPostiDisponibili(), organizzatore);

        return eventiRepository.save(evento);

    }

    public Evento findById(UUID eventoId){
        return this.eventiRepository.findById(eventoId).orElseThrow(() -> new NotFoundException(eventoId));
    }
    public List<Evento> findByOrganizzatore(Utente utente){
        return eventiRepository.findByOrganizzatore(utente);
    }

    public Evento findByIdAndUpdate(UUID  eventoId, EventoDTO updatedEventoDTO){
        Evento found = findById(eventoId);


        LocalDate dataEvento = null;
        try {
            dataEvento= LocalDate.parse(updatedEventoDTO.dataEvento());
        }catch (Exception e){
            throw new BadRequestException("Errore: il formato data inserito non è corretto. inserire un formato data (yyyy-mm-dd)");
        }

//        UUID uuidUtente;
//        try {
//            uuidUtente=  UUID.fromString(updatedEventoDTO.organizzatore());
//        }catch (Exception e){
//            throw new BadRequestException("L'id inserito non è valido! Necessario inserire un ID di Tipo UUID");
//        }
//        Utente organizzatore = utentiRepository.findById(uuidUtente).orElseThrow(() ->  new NotFoundException(uuidUtente));


        found.setTitolo(updatedEventoDTO.titolo());
        found.setDescrizione(updatedEventoDTO.descrizione());
        found.setDataEvento(dataEvento);
        found.setNrPostiDisponibili(updatedEventoDTO.nrPostiDisponibili());
        found.setLuogo(updatedEventoDTO.luogo());


        return this.eventiRepository.save(found);
    }

    public void findByIdAndDelete(UUID  viaggioId){
        Evento found = findById(viaggioId);
        this.eventiRepository.delete(found);
    }
}
