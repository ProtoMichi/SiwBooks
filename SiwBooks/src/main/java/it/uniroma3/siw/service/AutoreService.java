package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.transaction.Transactional;

/**
 * Service per la gestione delle operazioni sugli autori.
 * Fornisce metodi per creare, leggere, aggiornare ed eliminare autori,
 * oltre a gestire fotografie e associazioni con libri.
 */
@Service
public class AutoreService {
    
    @Autowired
    private AutoreRepository autoreRepository;
    
    @Autowired
    private LibroRepository libroRepository;
    
    /**
     * Restituisce la lista completa di tutti gli autori presenti nel database.
     * 
     * @return lista di tutti gli autori
     */
    public List<Autore> getAllAutori() {
        return (List<Autore>) autoreRepository.findAll();
    }
    
    /**
     * Cerca un autore tramite il suo identificativo univoco.
     * Se non viene trovato nessun autore con l'id specificato,
     * viene sollevata una RuntimeException.
     * 
     * @param id l'id dell'autore da cercare
     * @return l'autore trovato
     * @throws RuntimeException se l'autore non è trovato
     */
    public Autore getAutoreById(Long id) {
        return autoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autore non trovato"));
    }
    
    /**
     * Salva un nuovo autore nel database. Se viene fornita una fotografia,
     * questa viene salvata come array di byte nell'entità Autore.
     * 
     * @param autore l'autore da salvare
     * @param fotografia un file immagine opzionale associato all'autore
     * @return l'autore salvato con eventuale fotografia
     * @throws RuntimeException in caso di errori durante la lettura del file fotografia
     */
    public Autore saveAutore(Autore autore, MultipartFile fotografia) {
        setFotografiaSePresente(autore, fotografia);
        return autoreRepository.save(autore);
    }
    
    /**
     * Aggiorna un autore esistente identificato dall'id.
     * I campi dell'autore vengono sovrascritti con quelli forniti,
     * e se è fornita una nuova fotografia, questa viene aggiornata.
     * 
     * @param id l'id dell'autore da aggiornare
     * @param autoreModificato autore con i nuovi dati da aggiornare
     * @param nuovaFoto nuova fotografia opzionale
     * @return l'autore aggiornato e salvato
     * @throws RuntimeException se l'autore non è trovato o si verifica un errore caricando la fotografia
     */
    public Autore updateAuthor(Long id, Autore autoreModificato, MultipartFile nuovaFoto) {
        Autore autore = getAutoreById(id);
        
        autore.setNome(autoreModificato.getNome());
        autore.setCognome(autoreModificato.getCognome());
        autore.setDataDiNascita(autoreModificato.getDataDiNascita());
        autore.setDataDiMorte(autoreModificato.getDataDiMorte());
        autore.setNazionalita(autoreModificato.getNazionalita());
        
        setFotografiaSePresente(autore, nuovaFoto);
        
        return autoreRepository.save(autore);
    }
    
    /**
     * Metodo di supporto privato che imposta la fotografia sull'autore
     * se il file fornito non è nullo e non è vuoto.
     * 
     * @param autore l'autore su cui impostare la fotografia
     * @param foto file immagine da impostare
     * @throws RuntimeException se si verifica un errore durante la lettura del file
     */
    private void setFotografiaSePresente(Autore autore, MultipartFile foto) {
        if (foto != null && !foto.isEmpty()) {
            try {
                autore.setFotografia(foto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Errore nel caricamento della fotografia", e);
            }
        }
    }
    
    /**
     * Elimina un autore dal database, rimuovendo prima tutte le associazioni
     * con i libri che lo contengono per evitare problemi di integrità referenziale.
     * 
     * Il metodo è annotato con {@link Transactional} per garantire
     * che l'intera operazione sia atomica.
     * 
     * @param id l'id dell'autore da eliminare
     * @throws RuntimeException se l'autore non è trovato
     */
    @Transactional
    public void deleteAutore(Long id) {
        Autore autore = getAutoreById(id);
        
        
        List<Libro> libriConAutore = libroRepository.findByAutori_Id(id);
        for (Libro libro : libriConAutore) {
            libro.getAutori().remove(autore);
        }
        autoreRepository.delete(autore);
    }
    
    /**
     * Restituisce l'immagine (fotografia) associata a un autore tramite id.
     * 
     * @param id l'id dell'autore
     * @return array di byte contenente la fotografia
     * @throws RuntimeException se l'autore non è trovato
     */
    public byte[] getFotografiaById(Long id) {
        return getAutoreById(id).getFotografia();
    }
    
    /**
     * Restituisce una lista di autori dati i loro identificativi.
     * 
     * @param ids lista di id degli autori da recuperare
     * @return lista di autori corrispondenti agli id
     */
    public List<Autore> findAllById(List<Long> ids) {
        return (List<Autore>) autoreRepository.findAllById(ids);
    }
    
    /**
     * Cerca autori il cui cognome contiene una stringa specifica,
     * ignorando maiuscole/minuscole.
     * 
     * @param cognome parte o intero cognome da cercare
     * @return lista di autori che corrispondono al criterio di ricerca
     */
    public List<Autore> cercaPerCognome(String cognome) {
        return autoreRepository.findByCognomeContainingIgnoreCase(cognome);
    }
}
