package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Autore;

/**
 * Repository per la gestione degli oggetti di tipo Autore.
 * Estende CrudRepository per fornire operazioni CRUD di base (Create, Read, Update, Delete).
 */
public interface AutoreRepository extends CrudRepository<Autore, Long> {

    /**
     * Trova tutti gli autori il cui cognome contiene la stringa specificata (case-insensitive).
     * 
     * Esempio: findByCognomeContainingIgnoreCase("ver") restituirà autori con cognomi come "Verdi", "Lovera", ecc.
     * 
     * @param cognome la sottostringa da cercare nel cognome
     * @return lista di autori corrispondenti
     */
    public List<Autore> findByCognomeContainingIgnoreCase(String cognome);
}
