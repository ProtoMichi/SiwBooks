package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.UtenteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller per la gestione dell'autenticazione,
 * registrazione utente e visualizzazione della home page.
 */
@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private LibroService libroService;

    @Autowired
	private AutoreService autoreService;

    /**
     * Mostra la pagina di login.
     *
     * @return login.html cioè la pagina di login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    /**
     * Mostra il form di registrazione utente.
     * @param model il modello della vista
     * @return registrazione.html cioè la pagina di registrazione
     */
    @GetMapping("/registrati")
    public String mostraFormRegistrazione(Model model) {
        Credentials credentials = new Credentials();
        credentials.setUser(new User());
        model.addAttribute("credentials", credentials);
        return "registrazione.html";
    }

    /**
     * Gestisce la registrazione di un nuovo utente.
     * Controlla la validità dei dati, la corrispondenza delle password e
     * se l'username è già esistente.
     *
     * @param credentials i dati delle credenziali dell'utente
     * @param bindingResult il risultato della validazione
     * @param confirmPassword la password di conferma
     * @param model il modello della vista
     * @return redirect alla pagina di login se successo, altrimenti ritorna al form di registrazione
     */
    @PostMapping("/registrati")
    public String registraUtente(
            @ModelAttribute("credentials") @Validated Credentials credentials,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "registrazione.html";
        }

        if (!credentials.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Le password non corrispondono");
            return "registrazione.html";
        }

        if (credentialsService.usernameEsistente(credentials.getUsername())) {
            model.addAttribute("erroreUsername", "Username già esistente");
            return "registrazione.html";
        }

        credentialsService.saveCredentials(credentials);
        return "redirect:/login";
    }

    /**
     * Mostra la home page con la lista dei libri.
     * @param model il modello della vista
     * @param authentication l'oggetto di autenticazione corrente
     * @return il nome della view per la home page
     */
    @GetMapping("/")
    public String homePage(Model model, Authentication authentication) {
        model.addAttribute("libri", libroService.getAllLibri());

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            String displayName = estraiDisplayName(authentication);
            String ruolo = estraiRuolo(authentication, displayName);
            model.addAttribute("utente", new UtenteView(displayName, ruolo));
        } else {
            model.addAttribute("utente", null);
        }
        return "index.html";
    }
    
    /**
	 * Mostra la lista di tutti gli autori.
	 * 
	 * @param model     modello per la vista
	 * @param principal utente autenticato (se presente)
	 * @return nome della vista per la lista degli autori
	 */
	@GetMapping("/autori")
	public String mostraAutori(Model model, Authentication authentication) {
		model.addAttribute("autori", autoreService.getAllAutori());
		if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            String displayName = estraiDisplayName(authentication);
            String ruolo = estraiRuolo(authentication, displayName);
            model.addAttribute("utente", new UtenteView(displayName, ruolo));
        } else {
            model.addAttribute("utente", null);
        }
		return "autori.html";
	}


    /**
     * Estrae l' username (displayName) dall'oggetto Authentication.
     * Supporta autenticazione OIDC, UserDetails, o semplice String.
     * @param authentication l'oggetto di autenticazione
     * @return il nome da mostrare all'utente
     */
    private String estraiDisplayName(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OidcUser oidcUser) {
            return oidcUser.getAttribute("email");
        } else if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            return authentication.getName();
        }
    }

    /**
     * Estrae il ruolo dell'utente da Authentication o usa ruolo di default.
     * @param authentication l'oggetto di autenticazione
     * @param username il nome utente da cercare nel database
     * @return il ruolo dell'utente
     */
    private String estraiRuolo(Authentication authentication, String username) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            Credentials cred = credentialsService.getCredentials(username);
            if (cred != null) {
                return cred.getRuolo();
            }
        }
        return Credentials.DEFAULT_ROLE;
    }
}
