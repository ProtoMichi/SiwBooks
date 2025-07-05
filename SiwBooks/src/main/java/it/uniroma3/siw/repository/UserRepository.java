package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;

/**
 * Repository per la gestione delle entità User.
 * Estende CrudRepository per fornire le operazioni CRUD di base su utenti (Create, Read, Update, Delete).
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
