package com.example.gestioneEventi_S7_L5.controllers;

import com.example.gestioneEventi_S7_L5.exceptions.BadRequestException;
import com.example.gestioneEventi_S7_L5.payloads.LoginDTO;
import com.example.gestioneEventi_S7_L5.payloads.NewDipendenteResp;
import com.example.gestioneEventi_S7_L5.payloads.UserLoginRespDTO;
import com.example.gestioneEventi_S7_L5.payloads.UtenteDTO;
import com.example.gestioneEventi_S7_L5.services.AuthService;
import com.example.gestioneEventi_S7_L5.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UtentiService utentiService;

    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody LoginDTO payload) {
        return new UserLoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // Serve per customizzare lo status code (CREATED --> 201)
    public NewDipendenteResp createAuthor(@RequestBody @Validated UtenteDTO utenteDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            // Se ci sono stati errori lanciamo un'eccezione custom
            String messages = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            // Se non ci sono stati salviamo l'utente
            return new NewDipendenteResp(utentiService.save(utenteDTO).getId());
        }

    }
}
