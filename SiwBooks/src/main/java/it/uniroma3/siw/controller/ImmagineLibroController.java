package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import it.uniroma3.siw.model.ImmagineLibro;
import it.uniroma3.siw.repository.ImmagineLibroRepository;
import it.uniroma3.siw.service.ImmagineLibroService;

/**
 * Controller per la gestione delle immagini associate ai libri.
 * Consente in oltre di ottenere l'immagine principale di un libro
 * e un'immagine specifica tramite ID.
 */
@Controller
public class ImmagineLibroController {

    @Autowired
    private ImmagineLibroRepository immagineLibroRepository;

    @Autowired
    private ImmagineLibroService immagineLibroService;

    /**
     * Restituisce l'immagine principale associata a un libro tramite il suo ID.
     * @param id ID del libro di cui si vuole ottenere l'immagine principale.
     * @return array di byte contenente i dati dell'immagine JPEG, oppure array vuoto se non esiste.
     */
    @Transactional
    @GetMapping(value = "/libri/{id}/immagine-principale", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] immagineLibroPrincipale(@PathVariable Long id) {
        ImmagineLibro immagineLibro = immagineLibroRepository.findFirstByImmagine2Libro_Id(id);

        if (immagineLibro != null && immagineLibro.getDatiImmagine() != null) {
            return immagineLibro.getDatiImmagine();
        }

        // Nessuna immagine trovata → restituisci array vuoto
        return new byte[0];
    }

    /**
     * Restituisce l'immagine corrispondente a un determinato ID immagine.
     * Se l'immagine non viene trovata o si verifica un errore, restituisce uno status HTTP appropriato.
     * 
     * @param id ID dell'immagine da recuperare.
     * @return ResponseEntity contenente i dati dell'immagine e gli header HTTP,
     *         o uno status di errore in caso di eccezioni.
     */
    @Transactional
    @GetMapping("/immagine/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImmagine(@PathVariable Long id) {
        try {
            byte[] imageData = immagineLibroService.getImmagineLibroById(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
