package com.example.gestioneEventi_S7_L5.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorPayload {
    private String message;
    private LocalDateTime timestamp;
}
