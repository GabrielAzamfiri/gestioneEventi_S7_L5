package com.example.gestioneEventi_S7_L5.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Evento {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String titolo;
    private String descrizione;
    private LocalDate dataEvento;
    private Integer nrPostiDisponibili;

    @ManyToOne
    @JoinColumn(name = "organizzatore")
    private Utente organizzatore;


    @JsonIgnore
    @OneToMany(mappedBy = "evento")
    private List<Prenotazione> prenotazioneList;

    public Evento(String titolo, String descrizione, LocalDate dataEvento, Integer nrPostiDisponibili, Utente organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataEvento = dataEvento;
        this.nrPostiDisponibili = nrPostiDisponibili;
        this.organizzatore = organizzatore;
    }
}
