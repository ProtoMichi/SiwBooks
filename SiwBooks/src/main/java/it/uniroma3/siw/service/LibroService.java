package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.ImmagineLibro;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.transaction.Transactional;

/**
 * Service che offre operazioni di CRUD per un libro, ricerca e gestione di immagini e autori associati.
 */
@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutoreRepository autoreRepository;

    @Autowired
    private AutoreService autoreService;

    /**
     * Recupera tutti i libri presenti nel database.
     * 
     * @return lista di tutti i libri
     */
    public List<Libro> getAllLibri() {
        return (List<Libro>) libroRepository.findAll();
    }

    /**
     * Recupera un libro dato il suo identificativo.
     * 
     * @param id identificativo del libro
     * @return il libro trovato
     * @throws IllegalArgumentException se il libro con l'id specificato non esiste
     */
    public Libro getLibroById(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro non trovato con id: " + id));
    }

    /**
     * Salva un nuovo libro nel database, gestendo anche il caricamento delle immagini
     * associate e il salvataggio degli autori con le loro fotografie.
     * 
     * @param libro il libro da salvare
     * @param immagini lista di file immagine da associare al libro
     * @return il libro salvato
     */
    @Transactional
    public Libro saveLibro(Libro libro, List<MultipartFile> immagini) {
        if (libro.getImmagini() == null) {
            libro.setImmagini(new ArrayList<>());
        }

        aggiungiImmaginiAlLibro(libro, immagini);
        salvaAutoriConFotografie(libro);

        return libroRepository.save(libro);
    }

    /**
     * Aggiorna un libro esistente, modificandone i dati base, gli autori, 
     * le immagini da rimuovere e le nuove immagini da aggiungere.
     * 
     * @param id identificativo del libro da aggiornare
     * @param libroModificato oggetto contenente i dati aggiornati
     * @param nuoveImmagini lista di nuove immagini da aggiungere
     * @param immaginiDaRimuovere lista degli ID delle immagini da eliminare
     * @return il libro aggiornato
     */
    @Transactional
    public Libro updateLibro(Long id, Libro libroModificato, List<MultipartFile> nuoveImmagini, List<Long> immaginiDaRimuovere) {
        Libro libro = getLibroById(id);

        libro.setTitolo(libroModificato.getTitolo());
        libro.setAnno(libroModificato.getAnno());

        aggiornaAutoriDelLibro(libro, libroModificato);

        if (immaginiDaRimuovere != null) {
            libro.getImmagini().removeIf(img -> immaginiDaRimuovere.contains(img.getId()));
        }
        
        aggiungiImmaginiAlLibro(libro, nuoveImmagini);

        return libroRepository.save(libro);
    }

    /**
     * Elimina un libro dato il suo identificativo.
     * 
     * @param id identificativo del libro da eliminare
     */
    public void deleteLibroById(Long id) {
        libroRepository.deleteById(id);
    }

    /**
     * Cerca libri il cui titolo contiene la stringa specificata (case insensitive).
     * Se la stringa è vuota o nulla, ritorna tutti i libri.
     * 
     * @param titolo stringa da cercare nel titolo dei libri
     * @return lista dei libri che corrispondono alla ricerca
     */
    public List<Libro> cercaPerTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            return getAllLibri();
        }
        return libroRepository.findByTitoloContainingIgnoreCase(titolo);
    }

    /**
     * Aggiunge immagini caricate al libro.
     * 
     * @param libro libro a cui associare le immagini
     * @param immagini lista di file immagine da aggiungere
     */
    private void aggiungiImmaginiAlLibro(Libro libro, List<MultipartFile> immagini) {
        if (immagini == null) return;

        for (MultipartFile file : immagini) {
            if (!file.isEmpty()) {
                try {
                    ImmagineLibro immagine = new ImmagineLibro();
                    immagine.setDatiImmagine(file.getBytes());
                    immagine.setImmagine2Libro(libro);
                    libro.getImmagini().add(immagine);
                } catch (IOException e) {
                    throw new RuntimeException("Errore nella lettura del file immagine del libro", e);
                }
            }
        }
    }

    /**
     * Salva gli autori associati al libro e aggiorna le fotografie se caricate.
     * 
     * @param libro libro contenente gli autori da salvare
     */
    private void salvaAutoriConFotografie(Libro libro) {
        if (libro.getAutori() == null) return;

        for (Autore autore : libro.getAutori()) {
            MultipartFile fotoFile = autore.getFotografiaFile();
            if (fotoFile != null && !fotoFile.isEmpty()) {
                try {
                    autore.setFotografia(fotoFile.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Errore nella lettura del file fotografia autore", e);
                }
            }
            autoreRepository.save(autore);
        }
    }

    /**
     * Aggiorna la lista degli autori di un libro.
     * @param libro libro da aggiornare
     * @param libroModificato oggetto contenente i dati aggiornati
     */
    private void aggiornaAutoriDelLibro(Libro libro, Libro libroModificato) {
        if (libroModificato.getAutori() == null) return;

        List<Autore> autoriAggiornati = new ArrayList<>();
        for (Autore autoreMod : libroModificato.getAutori()) {
            if (autoreMod.getId() != null) {
                MultipartFile nuovaFoto = autoreMod.getFotografiaFile();
                Autore autoreAggiornato = autoreService.updateAuthor(autoreMod.getId(), autoreMod, nuovaFoto);
                autoriAggiornati.add(autoreAggiornato);
            } else {
                autoreRepository.save(autoreMod);
                autoriAggiornati.add(autoreMod);
            }
        }
        libro.setAutori(autoriAggiornati);
    }
}
