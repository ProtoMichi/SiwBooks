package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Libro;

/**
 * Repository per la gestione delle entità Libro.
 * Estende CrudRepository per fornire le operazioni CRUD di base (Create, Read, Update, Delete).
 */
public interface LibroRepository extends CrudRepository<Libro, Long> {

    /**
     * Trova tutti i libri associati a un autore specifico, identificato dall'id.
     * 
     * @param autoreId l'id dell'autore
     * @return lista di libri scritti dall'autore con l'id specificato
     */
    public List<Libro> findByAutori_Id(Long autoreId);

    /**
     * Cerca libri il cui titolo contiene una determinata stringa, ignorando la differenza tra maiuscole e minuscole.
     * 
     * @param titolo parte del titolo da cercare
     * @return lista di libri che contengono la stringa specificata nel titolo
     */
    public List<Libro> findByTitoloContainingIgnoreCase(String titolo);

}
