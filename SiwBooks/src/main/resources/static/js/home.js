document.addEventListener("DOMContentLoaded", function () {
  // Recupera l'elemento del titolo del libro e memorizza il titolo di default
  const title = document.getElementById('titolo-libro');
  const defaultTitle = title ? title.textContent : '';

  // Gestione del click sul link di logout per inviare il form di logout
  const logoutLink = document.getElementById('logout-link');
  if (logoutLink) {
    logoutLink.addEventListener('click', function (event) {
      event.preventDefault();
      const logoutForm = document.getElementById('logout-form');
      if (logoutForm) {
        logoutForm.submit();
      }
    });
  }

  // Aggiunge l'evento per eseguire la ricerca alla pressione del tasto Enter nell'input di ricerca
  const input = document.getElementById("searchInput");
  if (input) {
    input.addEventListener("keyup", function (event) {
      if (event.key === "Enter") {
        cercaLibri();
      }
    });
  }

  // Funzione per aggiungere eventi hover alle immagini dei libri
  // Cambia il titolo del libro quando il mouse è sopra un'immagine
  function aggiungiEventiHover() {
    const images = document.querySelectorAll('.book-image');
    images.forEach(img => {
      img.addEventListener('mouseover', () => {
        if (title) title.textContent = img.getAttribute('alt');
      });
      img.addEventListener('mouseout', () => {
        if (title) title.textContent = defaultTitle;
      });
    });
  }

  // Inizializza gli eventi hover sulle immagini caricate inizialmente
  aggiungiEventiHover();

  // Funzione globale per cercare libri in base al testo inserito nell'input di ricerca
  window.cercaLibri = function () {
    const query = input.value.trim();
    const container = document.getElementById("libriContainer");

    // Se la query è vuota, ricarica la pagina per mostrare tutti i libri
    if (query === "") {
      location.reload();
      return;
    }

    // Effettua una chiamata fetch al server per ottenere i libri corrispondenti alla ricerca
    fetch("/ricercaLibro?titolo=" + encodeURIComponent(query))
      .then(res => res.json())
      .then(data => {
        container.innerHTML = "";

        // Se non ci sono risultati, mostra un messaggio e aggiorna le frecce di scorrimento
        if (data.length === 0) {
          container.innerHTML = "<p>Nessun libro trovato.</p>";
          updateArrows(); // Aggiorna pulsanti anche in questo caso
          return;
        }

        // Per ogni libro restituito crea una card con immagine e titolo
        data.forEach(libro => {
          const div = document.createElement("div");
          div.classList.add("book-card");

          div.innerHTML = `
            <a href="/libri/${libro.id}" class="book-link">
              <img src="/libri/${libro.id}/immagine-principale"
                   alt="${libro.titolo}"
                   class="book-image"
                   onerror="this.onerror=null; this.src='/images/LibroDefault.jpg';" />
              <div class="book-title-overlay">${libro.titolo}</div>
            </a>
          `;
          container.appendChild(div);
        });

        // Dopo aver inserito i nuovi libri, aggiungi nuovamente gli eventi hover
        aggiungiEventiHover();
        // Aggiorna le frecce di scorrimento
        updateArrows();
      });
  };

  // Variabili per container libri e pulsanti di scroll
  const container = document.getElementById('libriContainer');
  const btnLeft = document.getElementById('scroll-left');
  const btnRight = document.getElementById('scroll-right');

  // Funzione per aggiornare la visibilità delle frecce di scorrimento
  function updateArrows() {
    if (container && btnLeft && btnRight) {
      // Mostra freccia sinistra solo se si può scrollare a sinistra
      if (container.scrollLeft > 0) {
        btnLeft.style.display = 'block';
      } else {
        btnLeft.style.display = 'none';
      }

      // Mostra freccia destra solo se c’è contenuto nascosto a destra
      if (container.scrollLeft < container.scrollWidth - container.clientWidth) {
        btnRight.style.display = 'block';
      } else {
        btnRight.style.display = 'none';
      }
    }
  }

  // Eventi click sui pulsanti per scrollare il container libri
  if (btnLeft && container) {
    btnLeft.addEventListener('click', () => {
      container.scrollBy({ left: -300, behavior: 'smooth' });
    });
  }

  if (btnRight && container) {
    btnRight.addEventListener('click', () => {
      container.scrollBy({ left: 300, behavior: 'smooth' });
    });
  }

  // Aggiorna le frecce quando si scrolla il container
  if (container) {
    container.addEventListener('scroll', updateArrows);
  }

  // Aggiorna le frecce quando la finestra viene ridimensionata
  window.addEventListener('resize', updateArrows);

  // Chiama updateArrows solo dopo che tutta la pagina (immagini incluse) è caricata
  window.addEventListener('load', () => {
    updateArrows();
  });
});
