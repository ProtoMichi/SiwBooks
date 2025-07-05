package it.uniroma3.siw.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.LibroService;

/**
 * Controller REST per la ricerca di libri.
 */
@RestController
public class LibroRestController {
	
	@Autowired
	private LibroService libroService;

	/**
	 * Richiesta GET per cercare libri in base al titolo.
	 * 
	 * @param titolo il titolo o parte di esso da cercare (parametro query)
	 * @return lista di libri che contengono il titolo specificato (case insensitive)
	 */
	@GetMapping("/ricercaLibro")
    public List<Libro> cercaLibriPerTitolo(@RequestParam String titolo) {
        // Richiama il servizio per cercare libri per titolo e restituisce i risultati
        return this.libroService.cercaPerTitolo(titolo);
    }

}
