package com.example.gestioneEventi_S7_L5.services;


import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.enums.RuoloUtente;
import com.example.gestioneEventi_S7_L5.exceptions.BadRequestException;
import com.example.gestioneEventi_S7_L5.exceptions.NotFoundException;
import com.example.gestioneEventi_S7_L5.payloads.UtenteDTO;
import com.example.gestioneEventi_S7_L5.repositories.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtentiService {

    @Autowired
    private UtentiRepository utentiRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    public Page<Utente> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utentiRepository.findAll(pageable);
    }
    public Utente findByEmail(String email) {
        return utentiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Il dipendente con l'email " + email + " non è stato trovato!"));
    }

    public Utente save(UtenteDTO utenteDTO) {
        // 1. Verifico che l'email non sia già stata utilizzata
        this.utentiRepository.findByEmail(utenteDTO.email()).ifPresent(
                // 1.1 Se lo è triggero un errore (400 Bad Request)
                dipendente -> {
                    throw new BadRequestException("L'email " + utenteDTO.email() + " è già in uso!");
                }
        );
        RuoloUtente ruoloUtente;
        try {
            ruoloUtente = RuoloUtente.valueOf(utenteDTO.ruolo().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Errore: il ruolo specificato non esiste.");
        }

        Utente newUtente = new Utente(utenteDTO.email(), bcrypt.encode(utenteDTO.password()), ruoloUtente );

        // 3. Salvo lo User
        return this.utentiRepository.save(newUtente);
    }

    public Utente findById(UUID utenteId) {
        return this.utentiRepository.findById(utenteId).orElseThrow(() -> new NotFoundException(utenteId));
    }

    public Utente findByIdAndUpdate(UUID utenteId, UtenteDTO updatedUtenteDTO) {
        // 1. Controllo se l'email nuova è già in uso
        this.utentiRepository.findByEmail(updatedUtenteDTO.email()).ifPresent(
                // 1.1 Se lo è triggero un errore (400 Bad Request)
                dipendente -> {
                    throw new BadRequestException("L'email " + updatedUtenteDTO.email() + " è già in uso!");
                }
        );
        RuoloUtente ruoloUtente;
        try {
            ruoloUtente = RuoloUtente.valueOf(updatedUtenteDTO.ruolo().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Errore: il ruolo specificato non esiste.");
        }

        Utente found = this.findById(utenteId);

        found.setEmail(updatedUtenteDTO.email());
        found.setPassword(bcrypt.encode(updatedUtenteDTO.password()));
        found.setRuolo(ruoloUtente);

        return this.utentiRepository.save(found);
    }

    public void findByIdAndDelete(UUID utenteId) {
        Utente found = this.findById(utenteId);
        this.utentiRepository.delete(found);
    }
}
