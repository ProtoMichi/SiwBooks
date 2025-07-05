package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.transaction.Transactional;

/**
 * Service che gestisce la logica relativa agli utenti (User) e alle loro credenziali.
 * Fornisce metodi per recuperare, salvare e cercare utenti nel database.
 */
@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    /**
     * Recupera un utente dato il suo ID.
     * 
     * @param id l'id dell'utente
     * @return l'utente trovato, o lancia NoSuchElementException se non presente
     */
    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * Recupera tutti gli utenti presenti nel database.
     * 
     * @return un iterable contenente tutti gli utenti
     */
    @Transactional
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Salva o aggiorna un utente nel database.
     * 
     * @param user l'utente da salvare
     * @return l'utente salvato
     */
    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Cerca un utente a partire dal suo username nelle credenziali.
     * 
     * @param username lo username da cercare
     * @return l'utente corrispondente, oppure null se non trovato
     */
    @Transactional
    public User findUtenteByUsername(String username) {
        Credentials cred = credentialsRepository.findByUsername(username).orElse(null);

        if (cred != null) {
            return cred.getUser();
        }

        return null;
    }
}
