package com.example.gestioneEventi_S7_L5.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("L'elemento con id " + id + " non è stato trovato!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
