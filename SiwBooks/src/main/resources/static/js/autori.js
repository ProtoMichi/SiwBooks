document.addEventListener("DOMContentLoaded", function () {
    // Gestione del click sul link di logout
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

    // Seleziona il titolo della sezione e ne memorizza il testo originale
    const title = document.querySelector('.title-section h2');
    const defaultTitle = title ? title.textContent : '';

    // Funzione per aggiungere eventi hover alle card degli autori
    function aggiungiEventiHover() {
        const authorCards = document.querySelectorAll('.author-card');

        authorCards.forEach(card => {
            // Al passaggio del mouse cambia il titolo con il nome autore
            card.addEventListener('mouseover', () => {
                const authorName = card.querySelector('.author-name-overlay')?.textContent;
                if (title && authorName) title.textContent = authorName;
            });

            // Quando il mouse esce dalla card, ripristina il titolo originale
            card.addEventListener('mouseout', () => {
                if (title) title.textContent = defaultTitle;
            });
        });
    }

    // Richiama la funzione per aggiungere gli eventi hover inizialmente
    aggiungiEventiHover();

    // Funzione per cercare autori in base all'input di ricerca
    function cercaAutori() {
        const input = document.getElementById('searchInput');
        const query = input.value.trim();

        const galleryElement = document.querySelector('.author-gallery');
        const adminAddAuthor = galleryElement.querySelector('.add-author-container');

        // Se la query è vuota ricarica la pagina per ripristinare la galleria
        if (query === "") {
            location.reload();
            return;
        }

        // Chiamata fetch per ottenere dati autori filtrati dal server
        fetch('/ricercaAutore?cognome=' + encodeURIComponent(query))
            .then(response => response.json())
            .then(data => {
                // Svuota la galleria prima di inserire i nuovi risultati
                galleryElement.innerHTML = "";

                // Se non ci sono risultati, mostra messaggio e reinserisce il bottone admin
                if (data.length === 0) {
                    galleryElement.innerHTML = "<p>Nessun autore trovato.</p>";
                    if (adminAddAuthor) {
                        galleryElement.appendChild(adminAddAuthor);
                    }
                    updateArrows(); // aggiorna le frecce anche in caso di nessun risultato
                    return;
                }

                // Per ogni autore ricevuto, crea una card e la aggiunge alla galleria
                data.forEach(autore => {
                    const div = document.createElement('div');
                    div.classList.add('author-card');
                    div.innerHTML = `
                        <a href="/autori/${autore.id}">
                            <img src="/autore/foto/${autore.id}" alt="Foto Autore" class="author-image" onerror="this.onerror=null;this.src='/images/AutoreDefault.jpg';" />
                            <div class="author-name-overlay">${autore.nome} ${autore.cognome}</div>
                        </a>
                    `;
                    galleryElement.appendChild(div);
                });

                // Reinserisce il contenitore per aggiungere un autore, se presente
                if (adminAddAuthor) {
                    galleryElement.appendChild(adminAddAuthor);
                }

                // Riassegna gli eventi hover alle nuove card e aggiorna le frecce
                aggiungiEventiHover();
                updateArrows();
            })
            .catch(err => {
                console.error('Errore nella ricerca autori:', err);
            });
    }

    // Event listener per il bottone di ricerca (click)
    const searchButton = document.querySelector('.search-bar button');
    if (searchButton) {
        searchButton.addEventListener('click', cercaAutori);
    }

    // Event listener per input di ricerca (ad ogni modifica)
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', function () {
            // Esegue la ricerca se la lunghezza è >= 2 o se il campo è vuoto
            if (this.value.length >= 2 || this.value.length === 0) {
                cercaAutori();
            }
        });
    }

    // -----------------------------------
    // Gestione frecce per lo scroll laterale
    // -----------------------------------
    const gallery = document.querySelector('.author-gallery');
    const btnLeft = document.getElementById('scroll-left');
    const btnRight = document.getElementById('scroll-right');

    // Funzione per aggiornare la visibilità delle frecce in base alla posizione dello scroll
    function updateArrows() {
        if (gallery && btnLeft && btnRight) {
            // Mostra freccia sinistra solo se si può scorrere verso sinistra
            if (gallery.scrollLeft > 0) {
                btnLeft.style.display = 'block';
            } else {
                btnLeft.style.display = 'none';
            }

            // Mostra freccia destra solo se si può scorrere verso destra
            if (gallery.scrollLeft < gallery.scrollWidth - gallery.clientWidth) {
                btnRight.style.display = 'block';
            } else {
                btnRight.style.display = 'none';
            }
        }
    }

    // Event listener per il click sulla freccia sinistra: scrolla indietro di 300px
    if (btnLeft && gallery) {
        btnLeft.addEventListener('click', () => {
            gallery.scrollBy({ left: -300, behavior: 'smooth' });
        });
    }

    // Event listener per il click sulla freccia destra: scrolla avanti di 300px
    if (btnRight && gallery) {
        btnRight.addEventListener('click', () => {
            gallery.scrollBy({ left: 300, behavior: 'smooth' });
        });
    }

    // Aggiorna la visibilità delle frecce durante lo scroll della galleria
    if (gallery) {
        gallery.addEventListener('scroll', updateArrows);
    }

    // Aggiorna la visibilità delle frecce anche al ridimensionamento della finestra
    window.addEventListener('resize', updateArrows);

    // Chiamata a updateArrows SOLO dopo che la pagina e tutte le immagini sono state caricate
    window.addEventListener('load', () => {
        updateArrows();
    });
});
