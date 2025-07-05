document.addEventListener('DOMContentLoaded', () => {
  // Container che contiene i div degli autori
  const container = document.getElementById('autori-container');
  // Bottone per aggiungere un nuovo autore
  const addBtn = document.getElementById('aggiungi-autore-btn');

  // Funzione per aggiornare gli indici nei nomi degli input degli autori
  // Serve a mantenere la numerazione corretta dopo aggiunte o rimozioni
  function aggiornaIndici() {
    const autori = container.querySelectorAll('.autore');
    autori.forEach((div, index) => {
      div.querySelectorAll('input').forEach(input => {
        if (input.name.includes('.nome')) {
          input.name = `autori[${index}].nome`;
        } else if (input.name.includes('.cognome')) {
          input.name = `autori[${index}].cognome`;
        } else if (input.name.includes('.dataDiNascita')) {
          input.name = `autori[${index}].dataDiNascita`;
        } else if (input.name.includes('.dataDiMorte')) {
          input.name = `autori[${index}].dataDiMorte`;
        } else if (input.name.includes('.nazionalita')) {
          input.name = `autori[${index}].nazionalita`;
        } else if (input.name.includes('.fotografiaFile')) {
          input.name = `autori[${index}].fotografiaFile`;
        }
      });
    });
  }

  // Funzione per aggiornare l'attributo "required" sugli input degli autori
  // Se almeno una checkbox di autori esistenti è selezionata, i campi di testo non sono richiesti
  function aggiornaRequired() {
    // Seleziona tutte le checkbox per autori esistenti
    const checkboxAutoriEsistenti = document.querySelectorAll('input[name="autoriEsistentiIds"]');
    // Verifica se almeno una è selezionata
    const qualcheSelezionato = Array.from(checkboxAutoriEsistenti).some(cb => cb.checked);
    // Seleziona tutti gli input di tipo testo o data nel container autori
    const autoreInputs = container.querySelectorAll('input[type="text"], input[type="date"]');

    autoreInputs.forEach(input => {
      if (qualcheSelezionato) {
        // Se almeno una checkbox è selezionata, rimuove required
        input.removeAttribute('required');
      } else {
        // Se nessuna checkbox selezionata, imposta required solo per nome e cognome
        if (input.name.includes('.nome') || input.name.includes('.cognome')) {
          input.setAttribute('required', 'required');
        } else {
          input.removeAttribute('required');
        }
      }
    });
  }

  // Evento per aggiungere un nuovo autore dinamicamente
  addBtn.addEventListener('click', () => {
    // Conta quanti autori ci sono già per aggiornare correttamente gli indici
    const currentAutoriCount = container.querySelectorAll('.autore').length;

    // Crea un nuovo div autore con i relativi input e bottone rimuovi
    const nuovoAutore = document.createElement('div');
    nuovoAutore.classList.add('autore');

    nuovoAutore.innerHTML = `
      <input type="text" name="autori[${currentAutoriCount}].nome" placeholder="Nome" required />
      <input type="text" name="autori[${currentAutoriCount}].cognome" placeholder="Cognome" required />
      <input type="date" name="autori[${currentAutoriCount}].dataDiNascita" placeholder="Data di nascita" />
      <input type="date" name="autori[${currentAutoriCount}].dataDiMorte" placeholder="Data di morte" />
      <input type="text" name="autori[${currentAutoriCount}].nazionalita" placeholder="Nazionalità" />
      <input type="file" name="autori[${currentAutoriCount}].fotografiaFile" accept="image/*" />
      <button type="button" class="rimuovi-autore-btn">- Rimuovi</button>
    `;

    // Aggiunge il nuovo autore al container
    container.appendChild(nuovoAutore);

    // Evento per rimuovere un autore e aggiornare indici e required
    nuovoAutore.querySelector('.rimuovi-autore-btn').addEventListener('click', () => {
      nuovoAutore.remove();
      aggiornaIndici();
      aggiornaRequired();
    });

    // Aggiorna l'attributo required sugli input dopo l'aggiunta
    aggiornaRequired();
  });

  // Aggiunge evento change su tutte le checkbox autori esistenti per aggiornare required
  const checkboxAutoriEsistenti = document.querySelectorAll('input[name="autoriEsistentiIds"]');
  checkboxAutoriEsistenti.forEach(cb => {
    cb.addEventListener('change', aggiornaRequired);
  });

  // Inizializza lo stato required al caricamento della pagina
  aggiornaRequired();
});
