package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.LibroRepository;
import it.uniroma3.siw.repository.RecensioneRepository;

/**
 * Service che gestisce la logica relativa alle recensioni dei libri.
 * Permette di aggiungere, aggiornare, trovare e cancellare recensioni.
 */
@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private LibroRepository libroRepository;

    /**
     * Aggiunge una nuova recensione per un libro specifico da parte di un utente.
     * Se l'utente ha già recensito il libro, lancia un'eccezione.
     * 
     * @param libroId l'id del libro da recensire
     * @param nuovaRecensione la recensione da aggiungere
     * @param utente l'utente che scrive la recensione
     * @return la recensione salvata
     * @throws IllegalStateException se l'utente ha già recensito il libro
     */
    public Recensione addRecensione(Long libroId, Recensione nuovaRecensione, User utente) {
        Libro libro = libroRepository.findById(libroId).get();
        Optional<Recensione> esistente = recensioneRepository.findByLibroAndUtente(libro, utente);

        if (esistente.isPresent()) {
            throw new IllegalStateException("Hai già recensito questo libro");
        }

        nuovaRecensione.setId(null);
        nuovaRecensione.setLibro(libro);
        nuovaRecensione.setUtente(utente);

        return recensioneRepository.save(nuovaRecensione);
    }

    /**
     * Aggiorna una recensione esistente modificandone titolo, testo e voto.
     * 
     * @param recensioneId id della recensione da aggiornare
     * @param titolo nuovo titolo della recensione
     * @param testo nuovo testo della recensione
     * @param voto nuovo voto assegnato nella recensione
     * @return la recensione aggiornata
     * @throws IllegalArgumentException se la recensione non esiste
     */
    public Recensione aggiornaRecensione(Long recensioneId, String titolo, String testo, int voto) {
        Optional<Recensione> optionalRecensione = recensioneRepository.findById(recensioneId);

        if (!optionalRecensione.isPresent()) {
            throw new IllegalArgumentException("Recensione non trovata");
        }

        Recensione recensione = optionalRecensione.get();
        recensione.setTitolo(titolo);
        recensione.setTesto(testo);
        recensione.setVoto(voto);

        return recensioneRepository.save(recensione);
    }

    /**
     * Recupera tutte le recensioni associate a un determinato libro.
     * 
     * @param libroId id del libro per cui si vogliono ottenere le recensioni
     * @return lista di recensioni associate al libro
     */
    public List<Recensione> findRecensioniByLibro(Long libroId) {
        Libro libro = libroRepository.findById(libroId).get();
        return recensioneRepository.findAllByLibro(libro);
    }

    /**
     * Elimina la recensione con l'id specificato.
     * 
     * @param id id della recensione da eliminare
     */
    public void deleteRecensione(Long id) {
        recensioneRepository.deleteById(id);
    }
}
