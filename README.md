# 📚 SiwBooks

**SiwBooks** è un sistema informativo basato su web che permette la gestione e la consultazione di informazioni relative a **libri**, **autori** e **recensioni**. Realizzato con **Spring Boot** e un’interfaccia HTML/CSS moderna, è progettato per supportare tre tipi di utenti: **utenti generici**, **utenti registrati** e **amministratori**.

## ✨ Funzionalità principali

### Utente non autenticato (utente generico)
- ✅ Visualizza la lista dei libri e i dettagli di ciascun libro, comprese le recensioni
- ✅ Ricerca di libri per titolo
- ✅ Visualizza la lista degli autori e i dettagli di ciascun autore
- ✅ Ricerca di autori per cognome

### Utente autenticato (registrato)
- ✅ Inserisce una recensione per un libro (una per ciascun libro)
- ✅ Modifica la propria recensione esistente

### Amministratore
- ✅ Aggiunge un nuovo libro con uno o più autori e immagini
- ✅ Modifica i dati di un libro: titolo, anno, autori, immagini
- ✅ Elimina libri e recensioni degli utenti
- ✅ Aggiunge, modifica ed elimina autori (compresi i dati biografici e la fotografia)

---

## 🧱 Casi d’Uso Implementati

- 👤 **Utente generico**:
  - Visualizzazione lista e dettagli libri
  - Ricerca per titolo
  - Visualizzazione lista e dettagli autori
  - Ricerca per cognome

- 🔐 **Utente registrato**:
  - Inserimento recensione
  - Modifica recensione

- 🛠 **Amministratore**:
  - Inserimento nuovo libro
  - Modifica libro
  - Gestione autori (aggiunta, modifica, eliminazione)
  - Eliminazione recensioni

---

## 🗂️ Modello del dominio

### Libro
- Titolo
- Anno di pubblicazione
- Lista di immagini (relazione one-to-many con ImmagineLibro)
- Lista di autori (relazione many-to-many con Autore)
- Lista di recensioni (relazione one-to-many con Recensione)

### ImmagineLibro
- Dati Immagine (un array di byte che consente di memorizzare l'immagine nel database) 

### Autore
- Nome
- Cognome
- Data di nascita
- Eventuale data di morte
- Nazionalità
- Fotografia

### Recensione
- Titolo
- Voto (1–5)
- Testo
- Relazione many-to-one con libro
- Relazione many-to-one con utente

### User
- Nome
- Cognome
- Email
- Credenziali (Relazione one-to-many con Credentials)
- Lista di recensioni (relazione one-to-many)

### Credentials
- Username
- Ruolo (DEFAULT_ROLE, ADMIN)
- Utente (relazione one-to-one con User)

---

## ⚙️ Tecnologie utilizzate

- **Spring Boot** (MVC, Thymeleaf, JPA/Hibernate)
- **HTML + CSS**
- **Spring Security** (per autenticazione e autorizzazione)
- **PostgreSQL** (per il database)
- **Maven** (per la gestione delle dipendenze)
- **OAuth2** (per l'autenticazione tramite Google)
