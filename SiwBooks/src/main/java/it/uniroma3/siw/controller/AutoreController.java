package it.uniroma3.siw.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredentialsService;

/**
 * Controller per la gestione delle operazioni relative agli autori.
 * 
 * <p>Permette di visualizzare la lista degli autori, visualizzare i dettagli di un autore,
 * aggiungere, modificare ed eliminare autori (queste ultime operazioni riservate agli utenti con ruolo ADMIN).
 * Gestisce inoltre la visualizzazione delle fotografie degli autori.</p>
 * 
 * @author 
 */
@Controller
public class AutoreController {

	@Autowired
	private AutoreService autoreService;

	@Autowired
	private CredentialsService credentialsService;

	/**
	 * Mostra i dettagli di un autore dato il suo id.
	 * 
	 * @param id        id dell'autore
	 * @param model     modello per la vista
	 * @param principal utente autenticato (se presente)
	 * @return nome della vista per il dettaglio autore
	 */
	@GetMapping("/autori/{id}")
	public String dettaglioAutore(@PathVariable Long id, Model model, Principal principal) {
		Autore autore = autoreService.getAutoreById(id);
		if (autore == null) {
			return "redirect:/autori";
		}
		model.addAttribute("autore", autore);
		model.addAttribute("utente", getUtenteFromPrincipal(principal));
		return "autore.html";
	}

	/**
	 * Elimina un autore dato il suo id. Accessibile solo agli utenti con ruolo ADMIN.
	 * 
	 * @param id        id dell'autore da eliminare
	 * @param principal utente autenticato
	 * @return redirect alla lista degli autori oppure alla pagina di login se non autenticato o non autorizzato
	 */
	@PostMapping("/autori/{id}/elimina")
	public String eliminaAutore(@PathVariable Long id, Principal principal) {
	    if (!isAdmin(principal)) {
	        if (principal == null) {
	            return "redirect:/login";
	        } else {
	            return "redirect:/autori";
	        }
	    }
	    autoreService.deleteAutore(id);
	    return "redirect:/autori";
	}

	/**
	 * Mostra il form per aggiungere un nuovo autore. Accessibile solo agli utenti con ruolo ADMIN.
	 * 
	 * @param model     modello per la vista
	 * @param principal utente autenticato
	 * @return nome della vista per il form di aggiunta autore oppure redirect se non autorizzato
	 */
	@GetMapping("/autori/aggiungi")
	public String mostraFormAggiungiAutore(Model model, Principal principal) {
	    if (!isAdmin(principal)) {
	        if (principal == null) {
	            return "redirect:/login";
	        } else {
	            return "redirect:/autori";
	        }
	    }
	    model.addAttribute("autore", new Autore());
	    return "formAutore.html";
	}

	/**
	 * Aggiunge un nuovo autore con i dati inviati dal form. Accessibile solo agli utenti con ruolo ADMIN.
	 * 
	 * @param autore          dati dell'autore da aggiungere
	 * @param bindingResult   risultato della validazione
	 * @param fotografiaFile  file della fotografia dell'autore
	 * @param principal       utente autenticato
	 * @return redirect alla lista degli autori o ritorno al form in caso di errori di validazione o mancanza autorizzazioni
	 */
	@PostMapping("/autori/aggiungi")
	public String aggiungiAutore(@Validated @ModelAttribute Autore autore,
			BindingResult bindingResult,
			@RequestParam("fotografiaFile") MultipartFile fotografiaFile,
			Principal principal) {
		if (!isAdmin(principal)) {
			if (principal == null) {
				return "redirect:/login";
			} else {
				return "redirect:/autori";
			}
		}
		if (bindingResult.hasErrors()) {
			return "formAutore.html";
		}
		autoreService.saveAutore(autore, fotografiaFile);
		return "redirect:/autori";
	}

	/**
	 * Mostra il form per modificare un autore esistente. Accessibile solo agli utenti con ruolo ADMIN.
	 * 
	 * @param id        id dell'autore da modificare
	 * @param model     modello per la vista
	 * @param principal utente autenticato
	 * @return modificaAutore.html (il form di modifica) oppure redirect se non autorizzato o autore non trovato
	 */
	@GetMapping("/autori/{id}/modifica")
	public String mostraFormModificaAutore(@PathVariable Long id, Model model, Principal principal) {
		if (!isAdmin(principal)) {
			return principal == null ? "redirect:/login" : "redirect:/";
		}
		Autore autore = autoreService.getAutoreById(id);
		if (autore == null) {
			return "redirect:/";
		}
		model.addAttribute("autore", autore);
		return "modificaAutore.html";
	}

	/**
	 * Aggiorna i dati di un autore esistente. Accessibile solo agli utenti con ruolo ADMIN.
	 * 
	 * @param id               id dell'autore da aggiornare
	 * @param autoreModificato dati modificati dell'autore
	 * @param bindingResult    risultato della validazione
	 * @param model            modello per la vista
	 * @param principal        utente autenticato
	 * @return redirect al dettaglio autore o ritorno al form modificaAutore.html in caso di errori di validazione o mancanza autorizzazioni
	 */
	@PostMapping("/autori/{id}/modifica")
	public String aggiornaAutore(@PathVariable Long id,
			@Validated @ModelAttribute("autore") Autore autoreModificato,
			BindingResult bindingResult,
			Model model,
			Principal principal) {
		if (!isAdmin(principal)) {
			return principal == null ? "redirect:/login" : "redirect:/";
		}
		Autore autoreEsistente = autoreService.getAutoreById(id);
		if (autoreEsistente == null) {
			return "redirect:/";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("autore", autoreModificato);
			return "modificaAutore.html";
		}
		// Aggiorna i campi dell'autore esistente
		autoreEsistente.setNome(autoreModificato.getNome());
		autoreEsistente.setCognome(autoreModificato.getCognome());
		autoreEsistente.setDataDiNascita(autoreModificato.getDataDiNascita());
		autoreEsistente.setDataDiMorte(autoreModificato.getDataDiMorte());
		autoreEsistente.setNazionalita(autoreModificato.getNazionalita());

		autoreService.saveAutore(autoreEsistente, autoreModificato.getFotografiaFile());

		return "redirect:/autori/" + id;
	}

	/**
	 * Restituisce la fotografia di un autore tramite il suo id.
	 * 
	 * @param id id dell'autore
	 * @return risposta HTTP con i byte dell'immagine o NOT_FOUND se non presente
	 */
	@GetMapping("/autore/foto/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> getFotoAutore(@PathVariable Long id) {
		try {
			byte[] foto = autoreService.getFotografiaById(id);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Adatta se immagini sono PNG o altro formato
			return new ResponseEntity<>(foto, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Metodo di utilità per ottenere l'utente autenticato dal Principal.
	 * 
	 * @param principal Principal dell'utente
	 * @return User se autenticato, altrimenti null
	 */
	private User getUtenteFromPrincipal(Principal principal) {
		if (principal == null) {
			return null;
		}
		Credentials cred = credentialsService.getCredentials(principal.getName());
		if (cred == null) {
			return null;
		}
		return cred.getUser();
	}

	/**
	 * Metodo di utilità per verificare se l'utente è autenticato ed ha ruolo ADMIN.
	 * 
	 * @param principal Principal dell'utente
	 * @return true se utente è admin, false altrimenti
	 */
	private boolean isAdmin(Principal principal) {
		if (principal == null) {
			return false;
		}
		Credentials cred = credentialsService.getCredentials(principal.getName());
		return cred != null && "ADMIN".equals(cred.getRuolo());
	}
}
