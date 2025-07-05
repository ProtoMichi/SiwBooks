package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.ImmagineLibro;

/**
 * Repository per la gestione delle entità ImmagineLibro.
 * Estende CrudRepository per fornire le operazioni CRUD di base (Create, Read, Update, Delete).
 */
public interface ImmagineLibroRepository extends CrudRepository<ImmagineLibro, Long> {

    /**
     * Recupera la prima immagine associata a un libro dato l'id del libro.
     * 
     * @param libroId l'id del libro di cui si vuole trovare l'immagine
     * @return la prima ImmagineLibro associata al libro specificato
     */
    public ImmagineLibro findFirstByImmagine2Libro_Id(Long libroId);
}
