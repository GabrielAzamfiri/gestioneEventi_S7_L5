package com.example.gestioneEventi_S7_L5.controllers;


import com.example.gestioneEventi_S7_L5.entities.Prenotazione;
import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.exceptions.NotFoundException;
import com.example.gestioneEventi_S7_L5.payloads.PrenotazioneDTO;
import com.example.gestioneEventi_S7_L5.payloads.UtenteDTO;
import com.example.gestioneEventi_S7_L5.services.PrenotazioniService;
import com.example.gestioneEventi_S7_L5.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtentiController {
    @Autowired
    private UtentiService utentiService;
    @Autowired
    private PrenotazioniService prenotazioniService;

    @GetMapping()
    public Page<Utente> getAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return this.utentiService.findAll(page, size, sortBy);
    }

    @GetMapping("/{utenteId}")// Tutti gli utenti possono leggere il profilo di un altro utente
    public Utente getById(@PathVariable UUID utenteId) {
        return utentiService.findById(utenteId);
    }

    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        // Tramite @AuthenticationPrincipal posso accedere ai dati dell'utente che sta effettuando la richiesta
        return currentAuthenticatedUser;
    }
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.utentiService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @GetMapping("/me/prenotazioni")
    public List<Prenotazione> findMyPrenotazioni(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return prenotazioniService.findByUtente(currentAuthenticatedUser);
    }

    @PutMapping("/me/prenotazioni/{prenotazioneId}")
    public Prenotazione findMyPrenotazioneAndUpdate(@AuthenticationPrincipal Utente currentAuthenticatedUser, @PathVariable UUID prenotazioneId, @RequestBody @Validated PrenotazioneDTO updatedPrenotazioneDTO) {
        List<Prenotazione> prenotazioneList = prenotazioniService.findByUtente(currentAuthenticatedUser);

        //controllo che l'id passato sia uno della sua lista
        Prenotazione myPrenotazione = prenotazioneList.stream().filter(prenotazione -> prenotazione.getId()
                .equals(prenotazioneId)).findFirst().orElseThrow(() -> new NotFoundException(prenotazioneId));
        
        return prenotazioniService.findByIdAndUpdate(myPrenotazione.getId(), updatedPrenotazioneDTO);
    }

    @DeleteMapping("/me/prenotazioni/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    public void findMyPrenotazioneAndDelete(@AuthenticationPrincipal Utente currentAuthenticatedUser , @PathVariable UUID prenotazioneId) {
      List<Prenotazione> prenotazioneList = prenotazioniService.findByUtente(currentAuthenticatedUser);

      //controllo che l'id passato sia uno della sua lista
        Prenotazione myPrenotazione = prenotazioneList.stream().filter(prenotazione -> prenotazione.getId()
                .equals(prenotazioneId)).findFirst().orElseThrow(() -> new NotFoundException(prenotazioneId));

        prenotazioniService.findByIdAndDelete(myPrenotazione.getId());
    }



    @PutMapping("/{utenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Solo gli admin possono modificare altri utenti
    public Utente findByIdAndUpdate(@PathVariable UUID utenteId, @RequestBody @Validated UtenteDTO updatedUtenteDTO) {
        return utentiService.findByIdAndUpdate(utenteId, updatedUtenteDTO);
    }

    @DeleteMapping("/{utenteId}")
    @PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono modificare altri utenti
    @ResponseStatus(HttpStatus.NO_CONTENT) // Serve per customizzare lo status code (NO_CONTENT --> 204)
    public void findByIdAndDelete(@PathVariable UUID utenteId) {
        utentiService.findByIdAndDelete(utenteId);
    }
}
