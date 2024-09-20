package com.example.gestioneEventi_S7_L5.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UserLoginRespDTO(
        @NotEmpty(message = "Il accessToken è obbligatorio")
        String accessToken
) {
}
