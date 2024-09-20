package com.example.gestioneEventi_S7_L5.entities;

import com.example.gestioneEventi_S7_L5.enums.RuoloUtente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Utente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String email;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private RuoloUtente ruolo;

    @JsonIgnore
    @OneToMany(mappedBy = "organizzatore")
    private List<Evento> eventoList;

    @JsonIgnore
    @OneToMany(mappedBy = "utente")
    private List<Prenotazione> prenotazioneList;


    public Utente(String email, String password, RuoloUtente ruolo) {
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }
}
