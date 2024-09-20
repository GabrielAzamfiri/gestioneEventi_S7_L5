package com.example.gestioneEventi_S7_L5.controllers;

import com.example.gestioneEventi_S7_L5.entities.Evento;
import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.exceptions.BadRequestException;
import com.example.gestioneEventi_S7_L5.payloads.EventoDTO;
import com.example.gestioneEventi_S7_L5.services.EventiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventi")
public class EventiController {

    @Autowired
    private EventiService eventiService;


    @GetMapping()
    public Page<Evento> getAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return this.eventiService.findAll(page, size, sortBy);
    }

    @GetMapping("/{eventoId}")
    public Evento getById(@PathVariable UUID eventoId) {
        return eventiService.findById(eventoId);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ORGANIZZATORE_EVENTI')") // Solo gli admin possono modificare altri utenti
    @ResponseStatus(HttpStatus.CREATED)
    public Evento saveEvento(@RequestBody @Validated EventoDTO eventoDTO, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            // Se ci sono stati errori lanciamo un'eccezione custom
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente
            return eventiService.save(eventoDTO);
        }
    }

    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Evento findByIdAndUpdate(@PathVariable UUID eventoId, @RequestBody @Validated EventoDTO updatedEventoDTO) {
        return eventiService.findByIdAndUpdate(eventoId, updatedEventoDTO);
    }

    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID eventoId) {
        eventiService.findByIdAndDelete(eventoId);
    }


}
