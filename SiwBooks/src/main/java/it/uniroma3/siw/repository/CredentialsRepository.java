package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Credentials;

/**
 * Repository per la gestione degli oggetti Credentials.
 * Estende CrudRepository per fornire operazioni CRUD di base (Create, Read, Update, Delete).
 */
public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    /**
     * Cerca le credenziali in base allo username.
     * 
     * Restituisce un Optional che può contenere l'oggetto Credentials se trovato,
     * oppure essere vuoto se non esiste nessuna corrispondenza.
     * 
     * @param username lo username da cercare
     * @return Optional contenente le credenziali trovate oppure vuoto
     */
    public Optional<Credentials> findByUsername(String username);

}
