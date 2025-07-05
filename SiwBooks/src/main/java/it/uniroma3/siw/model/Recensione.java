package it.uniroma3.siw.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * La classe rappresenta una Recensione scritta da un utente per un libro.
 * Ogni recensione contiene un titolo, un voto, un testo, e fa riferimento
 * all'utente che l'ha scritta e al libro recensito.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"utente_id", "libro_id"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Recensione {

    /** Identificatore univoco della recensione */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Titolo della recensione */
    private String titolo;

    /** Voto assegnato nella recensione (es. da 1 a 5) */
    private Integer voto;

    /** Contenuto testuale della recensione */
    private String testo;

    /** 
     * Libro recensito (relazione molti-a-uno). 
     * Più recensioni possono riferirsi allo stesso libro.
     */
    @ManyToOne
    private Libro libro;

    /** 
     * Utente che ha scritto la recensione (relazione molti-a-uno). 
     * Un utente può scrivere più recensioni.
     */
    @ManyToOne
    private User utente;

    /**
     * Restituisce l'utente autore della recensione.
     * @return utente autore
     */
    public User getUtente() {
        return utente;
    }

    /**
     * Imposta l'utente autore della recensione.
     * @param utente utente autore da impostare
     */
    public void setUtente(User utente) {
        this.utente = utente;
    }

    /**
     * Restituisce il libro a cui si riferisce la recensione.
     * @return libro recensito
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * Imposta il libro a cui si riferisce la recensione.
     * @param libro libro recensito da impostare
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    /**
     * Restituisce l'ID univoco della recensione.
     * @return ID della recensione
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'ID della recensione.
     * @param id identificatore da assegnare
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il titolo della recensione.
     * @return titolo della recensione
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il titolo della recensione.
     * @param titolo titolo da assegnare
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Restituisce il voto assegnato nella recensione.
     * @return voto (da 1 a 5 tipicamente)
     */
    public Integer getVoto() {
        return voto;
    }

    /**
     * Imposta il voto assegnato nella recensione.
     * @param voto valore numerico del voto
     */
    public void setVoto(Integer voto) {
        this.voto = voto;
    }

    /**
     * Restituisce il testo della recensione.
     * @return contenuto testuale della recensione
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il testo della recensione.
     * @param testo contenuto da assegnare
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Due recensioni sono considerate uguali se hanno lo stesso id,
     * lo stesso titolo e lo stesso utente autore.
     * @param obj oggetto da confrontare
     * @return true se uguali, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Recensione other = (Recensione) obj;
        return Objects.equals(id, other.id) &&
               Objects.equals(titolo, other.titolo) &&
               Objects.equals(utente, other.utente);
    }

    /**
     * Calcola l'hashCode della recensione in base a id, titolo e utente.
     * @return valore hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, utente);
    }

    /**
     * Restituisce una rappresentazione testuale della recensione.
     * @return stringa con i principali attributi della recensione
     */
    @Override
    public String toString() {
        return "Recensione [id=" + id + ", titolo=" + titolo + ", voto=" + voto + ", testo=" + testo + "]";
    }
}
