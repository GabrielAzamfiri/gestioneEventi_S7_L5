package com.example.gestioneEventi_S7_L5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "utente")
    private Utente utente;


    @ManyToOne
    @JoinColumn(name = "evento")
    private Evento evento;

    public Prenotazione(Utente utente, Evento evento) {
        this.utente = utente;
        this.evento = evento;
    }
}
