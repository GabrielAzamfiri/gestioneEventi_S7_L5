package com.example.gestioneEventi_S7_L5.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Config {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.formLogin(http -> http.disable()); // Non voglio il form di login (avremo React per quello)
        httpSecurity.csrf(http -> http.disable()); // Non voglio la protezione da attacchi CSRF (per le nostre app non è necessaria, anzi
        // andrebbe a complicare anche il codice lato FE)
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Non voglio le sessioni (perché con
        // JWT non si utilizzano le sessioni)
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll()); // Questo evita di ricevere 401 su OGNI richiesta

        // httpSecurity.cors(Customizer.withDefaults()); // OBBLIGATORIA QUESTA IMPOSTAZIONE SE VOGLIAMO CHE PER I CORS VENGA UTILIZZATO IL BEAN SOTTOSTANTE

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(11);
        // 11 è il cosiddetto numero di rounds, ovvero quante volte viene eseguito l'algoritmo, ciò è utile per regolarne la velocità di esecuzione.
        // Più è lento, più saranno sicure le nostre password e ovviamente viceversa. Bisogna però sempre tenere in considerazione anche la UX,
        // quindi più è lento, peggiò sarà la UX. In sostanza bisogna trovare il giusto bilanciamento tra sicurezza e UX
        // 11 ad esempio significa che l'algoritmo verrà eseguito 2^11 volte cioè 2048 volte. Su un computer di medie prestazioni equivale a circa
        // 100/200 ms
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
       // configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://newFrontEnd.com"));

        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
