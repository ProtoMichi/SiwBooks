package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Rappresenta un utente del sistema.
 * Ogni utente ha un nome, un cognome, un'email, delle credenziali per l'accesso
 * e può scrivere più recensioni.
 */
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    /** Identificatore univoco dell'utente*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Nome dell'utente */
    private String nome;

    /** Cognome dell'utente */
    private String cognome;

    /** Email dell'utente*/
    private String email;

    /**
     * Relazione uno-a-uno con le credenziali dell'utente.
     * Campo inverso della relazione, mappato da `user` in Credentials.
     */
    @OneToOne(mappedBy = "user")
    private Credentials credentials;

    /**
     * Lista di recensioni scritte dall'utente.
     * Relazione uno-a-molti: un utente può scrivere più recensioni.
     * La relazione è gestita dalla proprietà {@code utente} nella classe {@link Recensione}.
     */
    @OneToMany(mappedBy = "utente")
    private List<Recensione> recensioni;

    /**
     * Restituisce l'ID dell'utente.
     * @return identificatore univoco dell'utente
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'ID dell'utente.
     * @param id identificatore da assegnare
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il nome dell'utente.
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     * @param nome nome da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     * @return cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     * @param cognome cognome da assegnare
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce l'email dell'utente.
     * @return email dell'utente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     * @param email indirizzo email da assegnare
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce le credenziali associate all'utente.
     * @return oggetto Credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Imposta le credenziali associate all'utente.
     * @param credentials oggetto Credentials da assegnare
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Restituisce la lista di recensioni scritte dall'utente.
     * @return lista di oggetti Recensione
     */
    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    /**
     * Imposta la lista di recensioni scritte dall'utente.
     * @param recensioni lista di oggetti Recensione
     */
    public void setRecensioni(List<Recensione> recensioni) {
        this.recensioni = recensioni;
    }

    /**
     * Calcola l'hashCode dell'utente in base a nome, cognome ed email.
     * Utile per collezioni che usano hashing.
     * @return valore hash dell'oggetto
     */
    @Override
    public int hashCode() {
        return Objects.hash(cognome, email, nome);
    }

    /**
     * Confronta due oggetti User per uguaglianza.
     * Due utenti sono considerati uguali se hanno stesso nome, cognome ed email.
     * @param obj oggetto da confrontare
     * @return true se i due utenti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        return Objects.equals(cognome, other.cognome) &&
               Objects.equals(email, other.email) &&
               Objects.equals(nome, other.nome);
    }

    /**
     * Restituisce una rappresentazione testuale dell'utente.
     * Utile per il debugging e la registrazione nei log.
     * @return stringa descrittiva dell'utente
     */
    @Override
    public String toString() {
        return "User [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", recensioni="
                + recensioni + "]";
    }
}
