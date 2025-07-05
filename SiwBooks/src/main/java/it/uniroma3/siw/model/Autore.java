package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;

/**
 * Rappresenta un autore nel sistema, con le sue informazioni anagrafiche
 * e la lista dei libri associati.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Autore {

	/** Identificatore univoco dell'autore*/
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	/** Nome dell'autore */
	private String nome;

	/** Cognome dell'autore */
	private String cognome;

	/** Data di nascita dell'autore */
	private LocalDate dataDiNascita;

	/** Data di morte dell'autore (può essere null se ancora vivente) */
	private LocalDate dataDiMorte;

	/** Nazionalità dell'autore */
	private String nazionalita;

	/** Fotografia dell'autore (salvata come array di byte) */
	@Lob
	private byte[] fotografia;

	/** 
	 * Variabile utilizzata per salvare un File temporaneo della fotografia caricato da un form HTML.
	 * Questo campo non è persistente per cui non viene salvato nel database.
	 */
	@Transient
	private MultipartFile fotografiaFile;

	/** Lista dei libri scritti da questo autore
	 * mappata tramite la proprietà {@code autori} nella classe {@link Libro} */
	@ManyToMany(mappedBy="autori")
	private List<Libro> libri;

	/**
	 * Restituisce la lista dei libri scritti da questo autore.
	 * @return lista di libri
	 */
	public List<Libro> getLibri() {
		return libri;
	}

	/**
	 * Imposta la lista dei libri scritti da questo autore.
	 * @param libri lista di libri da associare
	 */
	public void setLibri(List<Libro> libri) {
		this.libri = libri;
	}

	/**
	 * Restituisce l'ID dell'autore.
	 * @return id univoco
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Imposta l'ID dell'autore.
	 * @param id nuovo ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Restituisce il nome dell'autore.
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il nome dell'autore.
	 * @param nome nuovo nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Restituisce il cognome dell'autore.
	 * @return cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Imposta il cognome dell'autore.
	 * @param cognome nuovo cognome
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Restituisce la data di nascita dell'autore.
	 * @return data di nascita
	 */
	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

	/**
	 * Imposta la data di nascita dell'autore.
	 * @param dataDiNascita nuova data di nascita
	 */
	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	/**
	 * Restituisce la data di morte dell'autore.
	 * @return data di morte, o null
	 */
	public LocalDate getDataDiMorte() {
		return dataDiMorte;
	}

	/**
	 * Imposta la data di morte dell'autore.
	 * @param dataDiMorte nuova data di morte
	 */
	public void setDataDiMorte(LocalDate dataDiMorte) {
		this.dataDiMorte = dataDiMorte;
	}

	/**
	 * Restituisce la nazionalità dell'autore.
	 * @return nazionalità
	 */
	public String getNazionalita() {
		return nazionalita;
	}

	/**
	 * Imposta la nazionalità dell'autore.
	 * @param nazionalita nuova nazionalità
	 */
	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	/**
	 * Restituisce la fotografia dell'autore come array di byte.
	 * @return fotografia
	 */
	public byte[] getFotografia() {
		return fotografia;
	}

	/**
	 * Imposta la fotografia dell'autore.
	 * @param fotografia nuova fotografia in byte[]
	 */
	public void setFotografia(byte[] fotografia) {
		this.fotografia = fotografia;
	}

	/**
	 * Restituisce il file della fotografia (non persistente).
	 * @return file della fotografia
	 */
	public MultipartFile getFotografiaFile() {
		return fotografiaFile;
	}

	/**
	 * Imposta il file della fotografia (non persistente).
	 * @param fotografiaFile file immagine caricato
	 */
	public void setFotografiaFile(MultipartFile fotografiaFile) {
		this.fotografiaFile = fotografiaFile;
	}

	/**
	 * Calcola l’hash dell'autore basandosi su nome, cognome e nazionalità.
	 * @return valore hash
	 */
	@Override
	public int hashCode() {
		return Objects.hash(cognome, nazionalita, nome);
	}

	/**
	 * Confronta due autori in base a nome, cognome e nazionalità.
	 * @param obj oggetto da confrontare
	 * @return true se uguali, false altrimenti
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Autore other = (Autore) obj;
		return Objects.equals(cognome, other.cognome) &&
			   Objects.equals(nazionalita, other.nazionalita) &&
			   Objects.equals(nome, other.nome);
	}

	/**
	 * Rappresentazione testuale dell'autore (utile per il debug).
	 * @return stringa descrittiva
	 */
	@Override
	public String toString() {
		return "Autore [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", dataDiNascita=" + dataDiNascita
				+ ", dataDiMorte=" + dataDiMorte + ", nazionalita=" + nazionalita + ", fotografia="
				+ Arrays.toString(fotografia) + "]";
	}
}
