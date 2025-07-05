package it.uniroma3.siw.service;

/**
 * Classe di supporto che rappresenta una vista semplificata dell'utente,
 * contenente solo le informazioni necessarie per la visualizzazione:
 * il nome da mostrare (displayName) e il ruolo dell'utente.
 */
public class UtenteView {

    private String displayName;  // Nome utente da mostrare nell'interfaccia
    private String ruolo;        // Ruolo assegnato all'utente (es. ADMIN, USER)

    /**
     * Costruttore per creare un'istanza di UtenteView con displayName e ruolo.
     * 
     * @param displayName nome da mostrare per l'utente
     * @param ruolo ruolo dell'utente
     */
    public UtenteView(String displayName, String ruolo) {
        this.displayName = displayName;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce il nome da mostrare per l'utente.
     * @return il displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Restituisce il ruolo dell'utente.
     * 
     * @return il ruolo
     */
    public String getRuolo() {
        return ruolo;
    }
}
