package it.uniroma3.siw.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.service.AutoreService;

/**
 * Controller REST per la ricerca di autori.
 */
@RestController
public class AutoreRestController {

    @Autowired
    private AutoreService autoreService;

    /**
     * Richiesta GET per cercare autori in base al cognome.
     * 
     * @param cognome il cognome da cercare (passato come parametro query)
     * @return lista di autori che corrispondono al cognome
     */
    @GetMapping("/ricercaAutore")
    public List<Autore> cercaAutorePerCognome(@RequestParam String cognome) {
        // Invoca il servizio per cercare autori per cognome e ritorna la lista trovata
        return this.autoreService.cercaPerCognome(cognome);
    }
}
