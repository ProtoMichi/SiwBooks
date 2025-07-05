package it.uniroma3.siw.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Entità che rappresenta le credenziali di accesso di un utente.
 * Contiene username, password, ruolo e il riferimento all'entità User.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Credentials {

	/** Costante per il ruolo utente standard */
	public static final String DEFAULT_ROLE = "USER";

	/** Costante per il ruolo amministratore */
	public static final String ADMIN_ROLE = "ADMIN";
	
	/** Identificatore univoco delle credenziali*/
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	/** Nome utente utilizzato per l'autenticazione */
	private String username;
	
	/** Password dell'utente*/
	private String password;
	
	/** Ruolo associato all'utente (es. USER, ADMIN) */
	private String ruolo;

	/** Riferimento all'utente associato a queste credenziali */
	@OneToOne(cascade = CascadeType.ALL)
	private User user;

	/**
	 * Restituisce l'id delle credenziali.
	 * @return id identificativo
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Imposta l'id delle credenziali.
	 * @param id identificativo da assegnare
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Restituisce lo username associato.
	 * @return nome utente
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Imposta lo username.
	 * @param username nome utente da assegnare
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Restituisce la password dell'utente.
	 * @return password (in chiaro o cifrata)
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Imposta la password dell'utente.
	 * @param password nuova password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Restituisce il ruolo dell'utente.
	 * @return ruolo (es. USER, ADMIN)
	 */
	public String getRuolo() {
		return ruolo;
	}

	/**
	 * Imposta il ruolo dell'utente.
	 * @param ruolo ruolo da assegnare
	 */
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	/**
	 * Restituisce l'entità utente associata.
	 * @return oggetto User collegato
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Imposta l'utente associato a queste credenziali.
	 * @param user oggetto User da associare
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
