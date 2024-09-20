package com.example.gestioneEventi_S7_L5.entities;

import com.example.gestioneEventi_S7_L5.enums.RuoloUtente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@JsonIgnoreProperties({ "authorities", "prenotazioneList", "eventoList", "username", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String email;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private RuoloUtente ruolo;


    @OneToMany(mappedBy = "organizzatore")
    private List<Evento> eventoList;


    @OneToMany(mappedBy = "utente")
    private List<Prenotazione> prenotazioneList;


    public Utente(String email, String password, RuoloUtente ruolo) {
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Questo metodo deve restituire una lista di ruoli dell'utente (SimpleGrantedAuthority, classe che in Spring rappresenta i ruoli degli utenti)
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
