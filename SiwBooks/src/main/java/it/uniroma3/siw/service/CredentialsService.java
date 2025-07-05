package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

/**
 * Service per la gestione delle credenziali di accesso degli utenti.
 * Gestisce il recupero, il salvataggio e la verifica dell'esistenza di username.
 */
@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder; 

    @Autowired
    protected CredentialsRepository credentialsRepository;

    /**
     * Recupera le credenziali tramite l'id univoco.
     * 
     * @param id identificativo delle credenziali
     * @return le credenziali se trovate, altrimenti null
     */
    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * Recupera le credenziali tramite username.
     * 
     * @param username username associato alle credenziali
     * @return le credenziali se trovate, altrimenti null
     */
    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    /**
     * Salva nuove credenziali nel database.
     * - Imposta l'id a null per creare un nuovo record
     * - Imposta il ruolo di default
     * - Codifica la password con il PasswordEncoder
     * 
     * @param credentials oggetto credenziali da salvare
     * @return le credenziali salvate con id generato
     */
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setId(null);
        credentials.setRuolo(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
    
    /**
     * Verifica se uno username esiste già nel database.
     * 
     * @param username username da controllare
     * @return true se esiste, false altrimenti
     */
    public boolean usernameEsistente(String username) {
        return credentialsRepository.findByUsername(username).isPresent();
    }
    
}
