package it.uniroma3.siw.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

/**
 * Entità che rappresenta un'immagine associata a un libro.
 * Ogni immagine è memorizzata come array di byte e può essere collegata a un libro.
 */
@Entity // Indica che questa classe è una entità persistente JPA
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
// Gestisce la serializzazione JSON per evitare loop infiniti in presenza di riferimenti ciclici
public class ImmagineLibro {

	/** Identificatore univoco dell'immagine*/
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	/** 
	 * Dati binari dell'immagine, memorizzati come Large Object (LOB).
	 * Adatto per memorizzare immagini in formato JPEG, PNG, ecc.
	 */
	@Lob
	private byte[] datiImmagine;

	/**
	 * Associazione molti-a-uno con l'entità {@link Libro}.
	 * Più immagini possono essere associate allo stesso libro.
	 */
	@ManyToOne
	private Libro immagine2Libro;

	/**
	 * Restituisce il libro associato a questa immagine.
	 * @return oggetto {@link Libro} associato
	 */
	public Libro getImmagine2Libro() {
		return immagine2Libro;
	}

	/**
	 * Imposta il libro a cui questa immagine è associata.
	 * @param immagine2Libro oggetto {@link Libro} da associare
	 */
	public void setImmagine2Libro(Libro immagine2Libro) {
		this.immagine2Libro = immagine2Libro;
	}

	/**
	 * Restituisce l'identificatore dell'immagine.
	 * @return id dell'immagine
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore dell'immagine.
	 * @param id identificatore da assegnare
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Restituisce i dati binari dell'immagine.
	 * @return array di byte dell'immagine
	 */
	public byte[] getDatiImmagine() {
		return datiImmagine;
	}

	/**
	 * Imposta i dati binari dell'immagine.
	 * @param datiImmagine array di byte da salvare
	 */
	public void setDatiImmagine(byte[] datiImmagine) {
		this.datiImmagine = datiImmagine;
	}

	/**
	 * Calcola il codice hash dell'oggetto basandosi sui dati dell'immagine.
	 * @return valore hash
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(datiImmagine);
		return result;
	}

	/**
	 * Verifica l'uguaglianza tra oggetti {@code ImmagineLibro}, basandosi sui dati binari dell'immagine.
	 * @param obj oggetto da confrontare
	 * @return true se le immagini sono uguali, false altrimenti
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmagineLibro other = (ImmagineLibro) obj;
		return Arrays.equals(datiImmagine, other.datiImmagine);
	}

	/**
	 * Restituisce una rappresentazione testuale dell'oggetto.
	 * @return stringa descrittiva contenente id, dati immagine e libro associato
	 */
	@Override
	public String toString() {
		return "ImmagineLibro [id=" + id + ", datiImmagine=" + Arrays.toString(datiImmagine) + ", immagine2Libro=" 
				+ immagine2Libro + "]";
	}
}
