package com.example.gestioneEventi_S7_L5.controllers;

import com.example.gestioneEventi_S7_L5.entities.Prenotazione;
import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.exceptions.BadRequestException;
import com.example.gestioneEventi_S7_L5.payloads.PrenotazioneDTO;
import com.example.gestioneEventi_S7_L5.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    @Autowired
    private PrenotazioniService prenotazioniService;


    @GetMapping()
    public Page<Prenotazione> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return this.prenotazioniService.findAll(page, size, sortBy);
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione getById(@PathVariable UUID prenotazioneId) {
        return prenotazioniService.findById(prenotazioneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(@RequestBody @Validated PrenotazioneDTO prenotazioneDTO, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            // Se ci sono stati errori lanciamo un'eccezione custom
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente
            return prenotazioniService.save(prenotazioneDTO);
        }
    }

    @PutMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Solo gli admin possono modificare altri utenti
    public Prenotazione findByIdAndUpdate(@PathVariable UUID prenotazioneId, @RequestBody @Validated PrenotazioneDTO updatedPrenotazioneDTO) {
        return prenotazioniService.findByIdAndUpdate(prenotazioneId, updatedPrenotazioneDTO);
    }

    @DeleteMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Solo gli admin possono modificare altri utenti
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
        prenotazioniService.findByIdAndDelete(prenotazioneId);
    }
}
