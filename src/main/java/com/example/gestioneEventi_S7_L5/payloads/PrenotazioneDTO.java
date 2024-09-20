package com.example.gestioneEventi_S7_L5.payloads;

import jakarta.validation.constraints.NotEmpty;

public record PrenotazioneDTO(
        @NotEmpty(message = "L'utente è obbligatorio")
        String utente,

        @NotEmpty(message = "L'evento è obbligatorio")
        String evento
) {
}
