document.addEventListener('DOMContentLoaded', () => {
    // Seleziona il contenitore degli autori e il pulsante "Aggiungi autore"
    const container = document.getElementById('autori-container');
    const addBtn = document.getElementById('aggiungi-autore-btn');

    // Event listener per la rimozione di un autore tramite event delegation
    container.addEventListener('click', (e) => {
        // Se si clicca su un pulsante con classe "rimuovi-autore-btn"
        if (e.target.classList.contains('rimuovi-autore-btn')) {
            // Rimuove il div genitore (che contiene gli input dell'autore)
            e.target.parentElement.remove();
            // Aggiorna gli indici dei campi rimasti
            aggiornaIndici();
        }
    });

    // Event listener per aggiungere un nuovo autore
    addBtn.addEventListener('click', () => {
        // Conta quanti autori ci sono già nel container
        const currentAutoriCount = container.querySelectorAll('.autore').length;

        // Crea un nuovo div per l'autore
        const nuovoAutore = document.createElement('div');
        nuovoAutore.classList.add('autore');

        // Inserisce gli input per il nuovo autore con i nomi indicizzati
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
    });

    // Funzione per aggiornare gli indici dei nomi degli input dopo una rimozione
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
                } else if (input.name.includes('.id')) {
                    input.name = `autori[${index}].id`;
                }
            });
        });
    }
});
