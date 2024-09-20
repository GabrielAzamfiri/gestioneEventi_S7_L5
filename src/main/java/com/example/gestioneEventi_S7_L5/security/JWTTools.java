package com.example.gestioneEventi_S7_L5.security;

import com.example.gestioneEventi_S7_L5.entities.Utente;
import com.example.gestioneEventi_S7_L5.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Utente utente) { // Dato un utente generami un token per esso
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // <-- Data di emissione del Token (IAT - Issued At), va messa in millisecondi
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // <-- Data di scadenza (Expiration Date), anche questa in millisecondi
                .subject(String.valueOf(utente.getId()))  // <-- Subject, ovvero a chi appartiene il token (ATTENZIONE A NON METTERE DATI SENSIBILI QUA DENTRO!!!!!!!!!)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // <-- Con questo firmo il token, per poterlo fare devo utilizzare un algoritmo apposito e il SEGRETO (che solo il server conosce)
                .compact();
    }

    public void verifyToken(String token) { // Dato un token verificami se è valido (non manipolato e non scaduto)
        // Per fare le verifiche useremo ancora una volta la libreria jsonwebtoken, la quale lancerà delle eccezioni in caso di problemi col token
        // Queste eccezioni dovranno "trasformarsi" in un 401
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            // Non importa se l'eccezione lanciata da .parse() sia dovuta a token scaduto oppure manipolato oppure malformato, per noi dovranno tutte
            // risultare in un 401
            System.out.println(ex.getMessage());
            throw new UnauthorizedException("Problemi col token! Per favore effettua di nuovo il login!");
        }
    }

    public String extractIdFromToken(String accessToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(accessToken).getPayload().getSubject(); // Il subject è l'id dell'utente
    }
}
