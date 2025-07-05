package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.ImmagineLibro;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.ImmagineLibroRepository;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.transaction.Transactional;

/**
 * Service per gestire le immagini associate ai libri.
 * 
 * <p>Fornisce operazioni per salvare, recuperare, eliminare immagini,
 * e per rimuovere immagini associate a uno specifico libro.</p>
 */
@Service
public class ImmagineLibroService {

    @Autowired
    private ImmagineLibroRepository immagineLibroRepository;

    @Autowired
    private LibroRepository libroRepository;

    /**
     * Salva una nuova immagine associata a un libro specifico.
     * 
     * @param libroId ID del libro a cui associare l'immagine
     * @param file file immagine caricato tramite MultipartFile
     * @return l'entità ImmagineLibro salvata nel database
     * @throws IOException se si verifica un errore nella lettura dei dati dal file
     * @throws RuntimeException se il libro con l'ID specificato non è stato trovato
     */
    public ImmagineLibro saveImmagineLibro(Long libroId, MultipartFile file) throws IOException {
        
        libroRepository.findById(libroId)
            .orElseThrow(() -> new RuntimeException("Libro non trovato"));

        
        ImmagineLibro immagine = new ImmagineLibro();
        immagine.setDatiImmagine(file.getBytes());

        return immagineLibroRepository.save(immagine);
    }

    /**
     * Recupera i dati binari di un'immagine dato il suo ID.
     * 
     * @param id ID dell'immagine da recuperare
     * @return array di byte contenente i dati dell'immagine
     * @throws IOException se l'immagine non viene trovata
     * @throws RuntimeException se l'immagine non esiste
     */
    public byte[] getImmagineLibroById(Long id) throws IOException {
        
        ImmagineLibro immagine = immagineLibroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Immagine non trovata"));

        return immagine.getDatiImmagine();
    }

    /**
     * Elimina un'immagine dato il suo ID.
     * 
     * @param id ID dell'immagine da eliminare
     */
    public void deleteImage(Long id) {
        immagineLibroRepository.deleteById(id);
    }

    /**
     * Rimuove una lista di immagini associate a un determinato libro.
     * 
     * <p>Per ogni immagine nella lista di ID fornita:
     * <ul>
     *   <li>Verifica se l'immagine esiste nel repository;</li>
     *   <li>Verifica se l'immagine è associata al libro;</li>
     *   <li>Rimuove l'immagine dalla lista delle immagini del libro;</li>
     *   <li>Elimina l'immagine dal database.</li>
     * </ul>
     * Infine salva il libro per aggiornare la relazione immagini.</p>
     * 
     * @param ids lista di ID delle immagini da rimuovere
     * @param libro libro da cui rimuovere le immagini
     */
    @Transactional
    public void rimuoviImmaginiById(List<Long> ids, Libro libro) {
        for (Long id : ids) {
            ImmagineLibro img = immagineLibroRepository.findById(id).orElse(null);
            if (img != null && libro.getImmagini().contains(img)) {
                libro.getImmagini().remove(img);
                immagineLibroRepository.delete(img);
            }
        }
        libroRepository.save(libro);
    }
}
