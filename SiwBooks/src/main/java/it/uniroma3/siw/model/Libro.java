package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

/**
 * Rappresenta un libro nel sistema.
 * Ogni libro ha un titolo, un anno di pubblicazione, una lista di immagini,
 * una lista di autori e una lista di recensioni.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Libro {
	
	/** Identificatore univoco del libro*/
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	/** Titolo del libro */
	private String titolo;

	/** Anno di pubblicazione del libro */
	private Integer anno;

	/**
	 * Immagini associate al libro.
	 * <ul>
	 *   <li>Mappate tramite la proprietà {@code immagine2Libro} nella classe {@link ImmagineLibro}</li>
	 *   <li>Caricate eagerly (FetchType.EAGER)</li>
	 *   <li>Cascade su persist e merge</li>
	 *   <li>Orphan removal abilitato: se rimosse dalla lista, vengono eliminate dal database</li>
	 * </ul>
	 */
	@OneToMany(mappedBy = "immagine2Libro", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ImmagineLibro> immagini;

	/**
	 * Lista degli autori associati al libro.
	 * <ul>
	 *   <li>Relazione molti-a-molti</li>
	 *   <li>Cascade su persist e merge</li>
	 *   <li>Caricamento eager</li>
	 * </ul>
	 */
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	private List<Autore> autori;

	/**
	 * Recensioni del libro.
	 * <ul>
	 *   <li>Mappate tramite la proprietà {@code libro} nella classe {@link Recensione}</li>
	 *   <li>Cascade su tutte le operazioni</li>
	 *   <li>Orphan removal abilitato</li>
	 * </ul>
	 */
	@OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Recensione> recensioni;

	/**
	 * Costruttore di default.
	 * Inizializza le liste di immagini, autori e recensioni come ArrayList vuoti.
	 */
	public Libro() {
	    this.autori = new ArrayList<>();
	    this.immagini = new ArrayList<>();
	    this.recensioni = new ArrayList<>();
	}

	/**
	 * Restituisce la lista degli autori del libro.
	 * @return lista di {@link Autore}
	 */
	public List<Autore> getAutori() {
		return autori;
	}

	/**
	 * Imposta la lista degli autori del libro.
	 * @param autori lista di {@link Autore}
	 */
	public void setAutori(List<Autore> autori) {
		this.autori = autori;
	}

	/**
	 * Restituisce la lista delle recensioni associate al libro.
	 * @return lista di {@link Recensione}
	 */
	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	/**
	 * Imposta la lista delle recensioni associate al libro.
	 * @param recensioni lista di {@link Recensione}
	 */
	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	/**
	 * Restituisce la lista delle immagini del libro.
	 * @return lista di {@link ImmagineLibro}
	 */
	public List<ImmagineLibro> getImmagini() {
		return immagini;
	}

	/**
	 * Imposta la lista delle immagini del libro.
	 * @param immagini lista di {@link ImmagineLibro}
	 */
	public void setImmagini(List<ImmagineLibro> immagini) {
		this.immagini = immagini;
	}

	/**
	 * Restituisce l'identificatore univoco del libro.
	 * @return id del libro
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Imposta l'identificatore univoco del libro.
	 * @param id identificatore da assegnare
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Restituisce il titolo del libro.
	 * @return titolo del libro
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * Imposta il titolo del libro.
	 * @param titolo titolo da assegnare
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * Restituisce l'anno di pubblicazione del libro.
	 * @return anno di pubblicazione
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * Imposta l'anno di pubblicazione del libro.
	 * @param anno anno da assegnare
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * Calcola il valore hash dell'oggetto basato su anno, autori e titolo.
	 * @return codice hash
	 */
	@Override
	public int hashCode() {
		return Objects.hash(anno, autori, titolo);
	}

	/**
	 * Verifica se due oggetti {@code Libro} sono uguali.
	 * Due libri sono considerati uguali se hanno stesso anno, autori e titolo.
	 * @param obj oggetto da confrontare
	 * @return true se uguali, false altrimenti
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return Objects.equals(anno, other.anno) && 
		       Objects.equals(autori, other.autori) &&
		       Objects.equals(titolo, other.titolo);
	}

	/**
	 * Ritorna una stringa descrittiva dell'oggetto {@code Libro}.
	 * Include id, titolo, anno, immagini, autori e recensioni.
	 * @return rappresentazione testuale
	 */
	@Override
	public String toString() {
		return "Libro [id=" + id + ", titolo=" + titolo + ", anno=" + anno +
		       ", immagini=" + immagini + ", autori=" + autori +
		       ", recensioni=" + recensioni + "]";
	}
}
