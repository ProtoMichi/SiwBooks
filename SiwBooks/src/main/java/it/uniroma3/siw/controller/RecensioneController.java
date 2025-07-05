package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;

/**
 * Controller che gestisce le operazioni relative alle recensioni dei libri,
 * come la visualizzazione del form per l'inserimento/modifica, la creazione,
 * la modifica e la cancellazione delle recensioni.
 * Gestisce inoltre l'autenticazione e i controlli di autorizzazione per
 * garantire che solo l'utente proprietario possa modificare le proprie recensioni.
 */
@Controller
public class RecensioneController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private CredentialsService credentialsService;

    /**
     * Mostra il form per aggiungere una nuova recensione ad un libro.
     * Aggiunge al modello il libro e un oggetto recensione vuoto.
     * Se l'utente è autenticato, aggiunge anche l'utente al modello.
     *
     * @param id l'id del libro da recensire
     * @param model modello MVC per passare dati alla view
     * @param authentication informazioni sull'utente autenticato
     * @return nome della pagina HTML con il form di recensione, oppure redirect se il libro non esiste
     */
    @GetMapping("/libri/{id}/aggiungiRecensione")
    public String mostraFormRecensione(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Libro libro = libroService.getLibroById(id);
        if (libro == null) {
            return "redirect:/libri";
        }
        model.addAttribute("libro", libro);
        model.addAttribute("recensione", new Recensione());

        User utente = getAuthenticatedUser(authentication);
        if (utente != null) {
            model.addAttribute("utente", utente);
        }

        return "formRecensione.html";
    }

    /**
     * Salva una nuova recensione per un libro specificato dall'id.
     * L'utente deve essere autenticato.
     * Se l'utente ha già recensito il libro, viene reindirizzato con errore.
     *
     * @param id id del libro
     * @param recensione oggetto recensione da salvare
     * @param authentication informazioni sull'utente autenticato
     * @return redirect alla pagina del libro o login se non autenticato
     */
    @PostMapping("/libri/{id}")
    public String salvaRecensione(@PathVariable("id") Long id,
                                  @ModelAttribute Recensione recensione,
                                  Authentication authentication) {
        User utente = getAuthenticatedUser(authentication);
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            recensioneService.addRecensione(id, recensione, utente);
        } catch (IllegalStateException e) {
            // Errore se l'utente ha già recensito il libro
            return "redirect:/libri/" + id + "?errore=gia_recensito";
        }

        return "redirect:/libri/" + id;
    }

    /**
     * Mostra il form per modificare una recensione esistente.
     * Controlla che l'utente autenticato sia l'autore della recensione.
     *
     * @param idLibro id del libro cui appartiene la recensione
     * @param idRecensione id della recensione da modificare
     * @param model modello MVC per passare dati alla view
     * @param authentication informazioni sull'utente autenticato
     * @return pagina di modifica recensione oppure redirect se non autorizzato o dati non trovati
     */
    @GetMapping("/libri/{idLibro}/modificaRecensione/{idRecensione}")
    public String mostraFormModificaRecensione(@PathVariable Long idLibro,
                                              @PathVariable Long idRecensione,
                                              Model model,
                                              Authentication authentication) {
        User utente = getAuthenticatedUser(authentication);
        if (utente == null) {
            return "redirect:/login";
        }

        Libro libro = libroService.getLibroById(idLibro);
        if (libro == null) {
            return "redirect:/libri";
        }

        Recensione recensioneDaModificare = null;
        for (Recensione r : libro.getRecensioni()) {
            if (r.getId().equals(idRecensione)) {
                recensioneDaModificare = r;
                break;
            }
        }

        if (recensioneDaModificare == null) {
            return "redirect:/libri/" + idLibro;
        }

        // Controllo autorizzazione: solo l'autore può modificare la recensione
        if (!recensioneDaModificare.getUtente().getId().equals(utente.getId())) {
            return "redirect:/libri/" + idLibro + "?errore=non_autorizzato";
        }

        model.addAttribute("libro", libro);
        model.addAttribute("recensione", recensioneDaModificare);
        model.addAttribute("utente", utente);

        return "modificaRecensione.html";
    }

    /**
     * Aggiorna la recensione con i nuovi dati forniti dall'utente.
     * Verifica che l'utente sia autenticato e abbia il diritto di modificare la recensione.
     *
     * @param idLibro id del libro cui appartiene la recensione
     * @param idRecensione id della recensione da aggiornare
     * @param titolo nuovo titolo della recensione
     * @param testo nuovo testo della recensione
     * @param voto nuovo voto della recensione
     * @param authentication informazioni sull'utente autenticato
     * @return redirect alla pagina del libro o login se non autenticato
     */
    @PostMapping("/libri/{idLibro}/modificaRecensione/{idRecensione}")
    public String aggiornaRecensione(@PathVariable Long idLibro,
                                     @PathVariable Long idRecensione,
                                     @RequestParam String titolo,
                                     @RequestParam String testo,
                                     @RequestParam int voto,
                                     Authentication authentication) {
        User utente = getAuthenticatedUser(authentication);
        if (utente == null) {
            System.out.println("Authentication è null!");
            return "redirect:/login";
        }
        try {
            recensioneService.aggiornaRecensione(idRecensione, titolo, testo, voto);
        } catch (IllegalArgumentException e) {
            return "redirect:/libri/" + idLibro + "/modificaRecensione/" + idRecensione + "?error=notfound";
        }

        return "redirect:/libri/" + idLibro;
    }

    /**
     * Elimina una o più recensioni di un libro, in base agli id ricevuti.
     *
     * @param idLibro id del libro cui appartengono le recensioni da eliminare
     * @param idsDaEliminare lista degli id delle recensioni da eliminare (opzionale)
     * @param authentication informazioni sull'utente autenticato
     * @return redirect alla pagina del libro
     */
    @PostMapping("/libri/{id}/eliminaRecensioni")
    public String eliminaRecensioni(@PathVariable("id") Long idLibro,
                                    @RequestParam(required = false, name = "recensioniDaEliminare") List<Long> idsDaEliminare,
                                    Authentication authentication) {
        User utente = getAuthenticatedUser(authentication);
        if (utente == null) {
            return "redirect:/login";
        }

        if (idsDaEliminare != null && !idsDaEliminare.isEmpty()) {
            for (Long id : idsDaEliminare) {
                recensioneService.deleteRecensione(id);
            }
        }
        return "redirect:/libri/" + idLibro;
    }

    /**
     * Estrae l'utente autenticato da Authentication, cercando l'username/email e
     * recuperando le credenziali corrispondenti.
     * Restituisce null se non autenticato o credenziali non trovate.
     *
     * @param authentication oggetto di Spring Security che rappresenta l'autenticazione corrente
     * @return oggetto User autenticato o null se non trovato
     */
    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        String identifier = null;

        if (principal instanceof OidcUser oidcUser) {
            identifier = oidcUser.getAttribute("email");
        } else if (principal instanceof UserDetails userDetails) {
            identifier = userDetails.getUsername();
        } else if (principal instanceof String str) {
            identifier = str;
        } else {
            identifier = authentication.getName();
        }

        if (identifier == null) {
            return null;
        }

        Credentials cred = credentialsService.getCredentials(identifier);
        if (cred == null) {
            return null;
        }

        return cred.getUser();
    }
}
