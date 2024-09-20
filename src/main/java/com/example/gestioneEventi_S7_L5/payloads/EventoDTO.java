package com.example.gestioneEventi_S7_L5.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EventoDTO(
        @NotEmpty(message = "Il titolo è obbligatorio")
        @Size(min = 3, max = 40, message = "Il titolo deve essere compreso tra 3 e 40 caratteri")
        String titolo,

        @NotEmpty(message = "La descrizione è obbligatoria")
        @Size(min = 3, max = 100, message = "La descrizione  deve essere compresa tra 3 e 100 caratteri")
        String descrizione,

        @NotEmpty(message = "La data Evento è obbligatoria")
        String dataEvento,

        @NotEmpty(message = "Il luogo è obbligatorio")
        String luogo,

        @NotNull( message = "Il nr Posti Disponibili è obbligatorio")
        int nrPostiDisponibili

        ) {
}
