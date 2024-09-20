package com.example.gestioneEventi_S7_L5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice

public class ExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    // Nelle parentesi indico quale eccezione debba venir gestita da questo metodo
    @ResponseStatus(HttpStatus.NOT_FOUND) // Lo status code deve essere 404
    public ErrorPayload handleNotFound(NotFoundException e) {
        // Questi handler mi consentono anche di accedere all'eccezione, utile per prendere il messaggio ad es
        return new ErrorPayload(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Lo status code deve essere 400
    public ErrorPayload handleBadRequest(BadRequestException ex) {

        return new ErrorPayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorPayload handleUnauthorized(UnauthorizedException ex) {
        return new ErrorPayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorPayload handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorPayload("Non hai i permessi per accedere", LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Lo status code deve essere 500
    public ErrorPayload handleGenericErrors(Exception ex) {
        ex.printStackTrace(); // Non dimentichiamoci che è ESTREMAMENTE UTILE sapere dove è stato generata l'eccezione per poterla fixare
        return new ErrorPayload("Problema lato server!! Prenditi una pausa <3", LocalDateTime.now());
    }
}
