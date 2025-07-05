package it.uniroma3.siw.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.service.*;

/**
 * Controller per la gestione delle operazioni relative ai libri.
 * Gestisce le richieste HTTP per visualizzare, aggiungere, modificare e eliminare libri.
 * 
 * Gestisce anche l'associazione tra libri, autori, immagini e recensioni.
 * Alcune operazioni sono riservate agli utenti con ruolo ADMIN.
 */
@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private ImmagineLibroService immagineLibroService;

    @Autowired
    private AutoreService autoreService;

    @Autowired
    private CredentialsService credentialsService;

    /**
     * Mostra la pagina di dettaglio di un libro identificato dall'id.
     * Carica immagini, autori e recensioni associati.
     * Se l'utente è autenticato, aggiunge al modello i dati dell'utente (nome e ruolo).
     * 
     * @param id l'id del libro da visualizzare
     * @param errore messaggio di errore opzionale da mostrare nella vista
     * @param model il modello dati per la vista
     * @param authentication informazioni sull'utente autenticato (se presente)
     * @return il nome della vista "libro.html"
     */
    @Transactional
    @GetMapping("/libri/{id}")
    public String dettaglioLibro(@PathVariable Long id,
                                 @RequestParam(value = "errore", required = false) String errore,
                                 Model model,
                                 Authentication authentication) {
        Libro libro = libroService.getLibroById(id);
        for (ImmagineLibro i : libro.getImmagini()) {
            i.getDatiImmagine();
        }
        libro.getAutori().size();
        libro.getRecensioni().size();

        model.addAttribute("libro", libro);
        model.addAttribute("autori", libro.getAutori());
        model.addAttribute("immagini", libro.getImmagini());
        model.addAttribute("recensioni", libro.getRecensioni());

        if (authentication != null) {
            String displayName = null;
            String ruolo = null;

            Object principalObj = authentication.getPrincipal();

            if (principalObj instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) principalObj;
                displayName = oidcUser.getAttribute("email");
                ruolo = Credentials.DEFAULT_ROLE;
            } else if (principalObj instanceof UserDetails) {
                displayName = ((UserDetails) principalObj).getUsername();
                Credentials credentials = credentialsService.getCredentials(displayName);
                if (credentials != null) ruolo = credentials.getRuolo();
            } else if (principalObj instanceof String) {
                displayName = (String) principalObj;
                ruolo = Credentials.DEFAULT_ROLE;
            } else {
                displayName = authentication.getName();
                ruolo = Credentials.DEFAULT_ROLE;
            }

            model.addAttribute("utente", new UtenteView(displayName, ruolo));
        } else {
            model.addAttribute("utente", null);
        }

        if (errore != null)
            model.addAttribute("errore", errore);

        return "libro.html";
    }

    /**
     * Mostra il form per aggiungere un nuovo libro.
     * Accessibile solo ad utenti autenticati con ruolo ADMIN.
     * 
     * @param model modello dati per la vista
     * @param principal informazioni sull'utente autenticato
     * @return il nome della vista "formLibro.html" oppure redirect se non autorizzato
     */
    @GetMapping("/libri/aggiungi")
    public String mostraFormAggiungiLibro(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        Credentials cred = credentialsService.getCredentials(principal.getName());
        if (cred == null || !"ADMIN".equals(cred.getRuolo()))
            return "redirect:/";

        model.addAttribute("libro", new Libro());
        model.addAttribute("autoriEsistenti", this.autoreService.getAllAutori());
        return "formLibro.html";
    }

    /**
     * Gestisce la richiesta POST per aggiungere un nuovo libro.
     * Valida il libro, gestisce gli autori (nuovi ed esistenti), le immagini e salva il libro.
     * 
     * @param libro l'oggetto Libro validato dal form
     * @param bindingResult risultato della validazione
     * @param immagini lista delle immagini caricate per il libro
     * @param autoriEsistentiIds lista di id di autori esistenti selezionati
     * @param principal informazioni sull'utente autenticato
     * @return redirect alla home page oppure il form in caso di errori
     */
    @PostMapping("/libri/aggiungi")
    public String aggiungiLibro(@Validated @ModelAttribute Libro libro,
                                BindingResult bindingResult,
                                @RequestParam("immagini") List<MultipartFile> immagini,
                                @RequestParam(value = "autoriEsistentiIds", required = false) List<Long> autoriEsistentiIds,
                                Principal principal) {

        if (libro.getAutori() != null) {
            List<Autore> autoriValidi = new ArrayList<>();
            for (Autore autore : libro.getAutori()) {
                if ((autore.getNome() != null && !autore.getNome().trim().isEmpty()) ||
                    (autore.getCognome() != null && !autore.getCognome().trim().isEmpty())) {
                    autoriValidi.add(autore);
                }
            }
            libro.setAutori(autoriValidi);
        }

        if (libro.getAutori() != null) {
            for (int i = 0; i < libro.getAutori().size(); i++) {
                Autore autore = libro.getAutori().get(i);
                MultipartFile foto = autore.getFotografiaFile();
                if (foto != null && !foto.isEmpty()) {
                    try {
                        autore.setFotografia(foto.getBytes());
                    } catch (IOException e) {
                        // log errore
                    }
                }
            }
        }

        if (autoriEsistentiIds != null && !autoriEsistentiIds.isEmpty()) {
            List<Autore> autoriEsistenti = autoreService.findAllById(autoriEsistentiIds);
            if (libro.getAutori() == null) libro.setAutori(new ArrayList<>());
            libro.getAutori().addAll(autoriEsistenti);
        }

        if (bindingResult.hasErrors()) {
            return "formLibro.html";
        }

        libroService.saveLibro(libro, immagini);
        return "redirect:/";
    }

    /**
     * Mostra il form per modificare un libro esistente identificato dall'id.
     * Accessibile solo ad utenti autenticati con ruolo ADMIN.
     * 
     * @param id l'id del libro da modificare
     * @param model modello dati per la vista
     * @param principal informazioni sull'utente autenticato
     * @return il nome della vista "modificaLibro.html" oppure redirect se non autorizzato
     */
    @GetMapping("/libri/{id}/modifica")
    public String mostraFormModificaLibro(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        Credentials cred = credentialsService.getCredentials(principal.getName());
        if (cred == null || !"ADMIN".equals(cred.getRuolo()))
            return "redirect:/";

        Libro libro = libroService.getLibroById(id);
        if (libro == null)
            return "redirect:/";

        List<Autore> tuttiAutori = autoreService.getAllAutori();
        List<Autore> autoriEsistenti = new ArrayList<>();

        for (Autore a : tuttiAutori) {
            boolean presente = false;
            for (Autore al : libro.getAutori()) {
                if (al.getId().equals(a.getId())) {
                    presente = true;
                    break;
                }
            }
            if (!presente) {
                autoriEsistenti.add(a);
            }
        }

        model.addAttribute("libro", libro);
        model.addAttribute("autoriEsistenti", autoriEsistenti);
        return "modificaLibro.html";
    }

    /**
     * Gestisce la richiesta POST per modificare un libro esistente.
     * Valida il libro modificato, gestisce le immagini da aggiungere/rimuovere, aggiorna gli autori.
     * 
     * @param id l'id del libro da modificare
     * @param libroModificato l'oggetto Libro validato con i dati modificati
     * @param bindingResult risultato della validazione
     * @param immagini nuove immagini da aggiungere
     * @param immaginiDaRimuovere lista di id delle immagini da rimuovere
     * @param autoriEsistentiIds lista di id di autori esistenti da aggiungere
     * @param principal informazioni sull'utente autenticato
     * @return redirect alla pagina di dettaglio del libro modificato oppure il form in caso di errori
     */
    @PostMapping("/libri/{id}/modifica")
    public String modificaLibro(@PathVariable Long id,
                                @Validated @ModelAttribute Libro libroModificato,
                                BindingResult bindingResult,
                                @RequestParam("immagini") List<MultipartFile> immagini,
                                @RequestParam(value = "immaginiDaRimuovere", required = false) List<Long> immaginiDaRimuovere,
                                @RequestParam(value = "autoriEsistentiIds", required = false) List<Long> autoriEsistentiIds,
                                Principal principal) {

        if (bindingResult.hasErrors()) return "modificaLibro.html";

        Libro libroEsistente = libroService.getLibroById(id);

        for (int i = 0; i < libroModificato.getAutori().size(); i++) {
            Autore autoreModificato = libroModificato.getAutori().get(i);

            Autore autoreOriginale = null;
            for (Autore a : libroEsistente.getAutori()) {
                if (a.getId() != null && a.getId().equals(autoreModificato.getId())) {
                    autoreOriginale = a;
                    break;
                }
            }

            MultipartFile foto = autoreModificato.getFotografiaFile();
            if (foto != null && !foto.isEmpty()) {
                try {
                    autoreModificato.setFotografia(foto.getBytes());
                } catch (IOException e) {
                    // log errore
                }
            } else if (autoreOriginale != null) {
                autoreModificato.setFotografia(autoreOriginale.getFotografia());
            }
        }

        if (immaginiDaRimuovere != null && !immaginiDaRimuovere.isEmpty()) {
            immagineLibroService.rimuoviImmaginiById(immaginiDaRimuovere, libroEsistente);
        }

        if (autoriEsistentiIds != null && !autoriEsistentiIds.isEmpty()) {
            List<Autore> autoriEsistenti = new ArrayList<>();
            for (Long autoreId : autoriEsistentiIds) {
                Autore autore = autoreService.getAutoreById(autoreId);
                if (autore != null) {
                    autoriEsistenti.add(autore);
                }
            }
            libroModificato.getAutori().addAll(autoriEsistenti);
        }

        libroService.updateLibro(id, libroModificato, immagini, immaginiDaRimuovere);
        return "redirect:/libri/" + id;
    }

    /**
     * Elimina un libro identificato dall'id.
     * Accessibile solo ad utenti autenticati con ruolo ADMIN.
     * 
     * @param id l'id del libro da eliminare
     * @param principal informazioni sull'utente autenticato
     * @return redirect alla home page oppure redirect al login se non autenticato
     */
    @PostMapping("/libri/{id}/elimina")
    public String eliminaLibro(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";

        Credentials cred = credentialsService.getCredentials(principal.getName());
        if (cred == null || !"ADMIN".equals(cred.getRuolo()))
            return "redirect:/";

        libroService.deleteLibroById(id);
        return "redirect:/";
    }

    /**
     * Configura il binder per ignorare il binding automatico del campo "immagini" nei modelli.
     * Evita che Spring tenti di bindare automaticamente le immagini dai form.
     * 
     * @param binder il WebDataBinder utilizzato per il binding
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("immagini");
    }
}
