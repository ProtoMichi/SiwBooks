package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;

/**
 * Repository per la gestione delle entità Recensione.
 * Estende CrudRepository per fornire le operazioni CRUD di base (Create, Read, Update, Delete).
 */
public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

    /**
     * Cerca una recensione specifica data da un utente su un libro.
     * 
     * @param libro il libro oggetto della recensione
     * @param utente l'utente che ha scritto la recensione
     * @return un Optional che contiene la recensione se esiste, altrimenti vuoto
     */
    public Optional<Recensione> findByLibroAndUtente(Libro libro, User utente);

    /**
     * Trova tutte le recensioni associate a un dato libro.
     * 
     * @param libro il libro di cui si vogliono le recensioni
     * @return lista di recensioni relative al libro specificato
     */
    public List<Recensione> findAllByLibro(Libro libro);

}
