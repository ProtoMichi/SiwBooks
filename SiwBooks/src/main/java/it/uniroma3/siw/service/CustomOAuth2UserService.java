package it.uniroma3.siw.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;

/**
 * Servizio personalizzato per gestire l'autenticazione OAuth2/OpenID Connect.
 * 
 * <p>
 * Questa classe estende {@link OidcUserService} per sovrascrivere il metodo
 * {@code loadUser}. Quando un utente si autentica tramite un provider OIDC,
 * questo servizio:
 * <ul>
 *   <li>recupera i dati dell'utente dal provider;</li>
 *   <li>verifica se l'utente esiste già nel database;</li>
 *   <li>se l'utente non esiste, crea e salva un nuovo utente con le
 *       credenziali di base;</li>
 *   <li>restituisce l'utente OIDC per completare il processo di autenticazione.</li>
 * </ul>
 * </p>
 */
@Service
public class CustomOAuth2UserService extends OidcUserService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    /**
     * Carica un utente OIDC dal provider e lo salva nel database se è la prima volta
     * che si autentica.
     *
     * @param userRequest la richiesta di autenticazione OIDC contenente i dati dell'utente
     * @return l'utente OIDC caricato con i dati forniti dal provider
     * @throws OAuth2AuthenticationException se si verifica un errore durante il caricamento dell'utente
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Recupera i dati dell'utente tramite il metodo della superclasse
        OidcUser oidcUser = super.loadUser(userRequest);

        // Estrai gli attributi dell'utente dal token OIDC
        Map<String, Object> attributes = oidcUser.getAttributes();

        // Preleva i campi essenziali dal token
        String email = (String) attributes.get("email");
        String nome = (String) attributes.get("given_name");
        String cognome = (String) attributes.get("family_name");

        // Controlla se esiste già un record di credenziali per questo utente
        Optional<Credentials> existingCredentials = credentialsRepository.findByUsername(email);

        if (existingCredentials.isEmpty()) {
            // Se l'utente non esiste, crea un nuovo record User e Credentials
            User user = new User();
            user.setEmail(email);
            user.setNome(nome);
            user.setCognome(cognome);

            Credentials credentials = new Credentials();
            credentials.setUsername(email);
            credentials.setPassword("");  // Password vuota perché autenticazione esterna
            credentials.setRuolo(Credentials.DEFAULT_ROLE);
            credentials.setUser(user);

            // Mantieni la relazione bidirezionale tra User e Credentials
            user.setCredentials(credentials);

            // Salva le credenziali (e cascata salva l'utente)
            credentialsRepository.save(credentials);
        }

        // Ritorna l'utente OIDC per completare l'autenticazione
        return oidcUser;
    }
}
