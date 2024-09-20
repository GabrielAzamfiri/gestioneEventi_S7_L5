package com.example.gestioneEventi_S7_L5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,

        @NotEmpty(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri")    //  @Pattern() Permette di inserire una Regular Expression per validare praticamente qualsiasi cosa (es. PW fatte in una certa maniera)
        String password,

        @NotEmpty(message = "Il ruolo è obbligatorio") @Size(min = 3, max = 40, message = "Il cognome deve essere compreso tra 3 e 40 caratteri")
        String ruolo
) {
}
