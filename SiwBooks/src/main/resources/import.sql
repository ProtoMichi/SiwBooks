-- Inserimento autori con immagini (large objects)
INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia) VALUES(nextval('autore_seq'),'Jules', 'Verne', '1828-02-08', '1905-03-24', 'Francese', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/verne_ritratto.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia)VALUES(nextval('autore_seq'),'Robert Louis', 'Stevenson', '1850-11-13', '1894-12-03', 'Scozzese', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Stevenson.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia)VALUES(nextval('autore_seq'),'Isaac', 'Asimov', '1920-01-02', '1992-04-06', 'Russa', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Isaac-Asimov.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia) VALUES(nextval('autore_seq'),'Oriana', 'Fallaci', '1929-06-29', '2006-09-15', 'Italiana', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Oriana_Fallaci.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia)VALUES(nextval('autore_seq'),'Umberto', 'Eco', '1932-01-05', '2016-02-19', 'Italiana', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Umberto_Eco.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia)VALUES(nextval('autore_seq'),'Jonathan', 'Swift', '1667-11-30', '1745-10-19', 'Irlandese', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Jonathan-Swift.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia) VALUES(nextval('autore_seq'),'Herman', 'Melville', '1819-08-01', '1891-09-28', 'Statunitense', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Herman_Melville.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia)VALUES(nextval('autore_seq'),'Lev Nikolàevič', 'Tolstòj', '1828-09-09', '1910-11-20', 'Russa', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Tolstoy.jpg'));

INSERT INTO autore (id,nome, cognome, data_di_nascita, data_di_morte, nazionalita, fotografia)VALUES(nextval('autore_seq'),'Tommaso', 'Moro', '1478-02-07', '1535-07-06', 'Inglese', lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Thomas_More.jpg'));



-- Inserimento libri
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Ventimila Leghe Sotto I Mari', 1870);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'L Isola Del Tesoro', 1883);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Io Robot', 1950);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Intervista con la Storia', 1974);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Il Nome della Rosa', 1980);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'I Viaggi di Gulliver', 1726);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Moby Dick', 1851);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Guerra e Pace', 1867);
INSERT INTO libro (id,titolo, anno) VALUES (nextval('libro_seq'),'Utopia', 1516);


-- Collegamento libro-autore (tabella many-to-many)
INSERT INTO libro_autori (libri_id, autori_id) SELECT l.id, a.id FROM libro l, autore a WHERE (l.titolo = 'Ventimila Leghe Sotto I Mari' AND a.nome = 'Jules' AND a.cognome = 'Verne') OR (l.titolo = 'L Isola Del Tesoro' AND a.nome = 'Robert Louis' AND a.cognome = 'Stevenson') OR (l.titolo = 'Io Robot' AND a.nome = 'Isaac' AND a.cognome = 'Asimov') OR (l.titolo = 'Intervista con la Storia' AND a.nome = 'Oriana' AND a.cognome = 'Fallaci') OR(l.titolo = 'Il Nome della Rosa' AND a.nome = 'Umberto' AND a.cognome = 'Eco') OR(l.titolo = 'I Viaggi di Gulliver' AND a.nome = 'Jonathan' AND a.cognome = 'Swift') OR(l.titolo = 'Moby Dick' AND a.nome = 'Herman' AND a.cognome = 'Melville') OR(l.titolo = 'Guerra e Pace' AND a.nome = 'Lev Nikolàevič' AND a.cognome = 'Tolstòj') OR(l.titolo = 'Utopia' AND a.nome = 'Tommaso' AND a.cognome = 'Moro');

-- Inserimento immagini libri (collegamento con libro tramite foreign key)
INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/ventimila-leghe-sotto-i-mari.jpg'), l.id FROM libro l WHERE l.titolo = 'Ventimila Leghe Sotto I Mari';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/L Isola Del Tesoro.jpg'), l.id FROM libro l WHERE l.titolo = 'L Isola Del Tesoro';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Io Robot.jpg'), l.id FROM libro l WHERE l.titolo = 'Io Robot';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Intervista_con_la_Storia.jpg'), l.id FROM libro l WHERE l.titolo = 'Intervista con la Storia';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Il_Nome_della_Rosa.jpg'), l.id FROM libro l WHERE l.titolo = 'Il Nome della Rosa';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/I_Viaggi_di_Gulliver.jpg'), l.id FROM libro l WHERE l.titolo = 'I Viaggi di Gulliver';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Moby_Dick.jpg'), l.id FROM libro l WHERE l.titolo = 'Moby Dick';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Guerra_e_Pace.jpg'), l.id FROM libro l WHERE l.titolo = 'Guerra e Pace';

INSERT INTO immagine_libro (id, dati_immagine, immagine2libro_id) SELECT nextval('immagine_libro_seq'), lo_import('D:/Sistemi Informativi su Web/SiwBooks/src/main/resources/images/Utopia.jpg'), l.id FROM libro l WHERE l.titolo = 'Utopia';





-- Inserimento utenti e relative credenziali
INSERT INTO users (id, nome, cognome, email) VALUES(nextval('users_seq'), 'Mario', 'Rossi', 'mario@example.com');
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('users_seq'), 'Lucia', 'Bianchi', 'lucia@example.com');
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('users_seq'), 'Direttore', 'Supremo', 'direttore@example.com');

-- Recupero degli id degli utenti appena creati (facoltativo, usa se il DB supporta RETURNING)
-- Altrimenti inserisci manualmente gli ID o usa sottoquery

-- Inserimento credenziali collegate agli utenti
INSERT INTO credentials (id, username, password, ruolo, user_id) VALUES (nextval('credentials_seq'), 'mario', '$2a$12$BwsgIwRvBRRkR3v5MeZwU.hLEK9N8y4WtF26KX9jFxNkop3foKLaK', 'USER', (SELECT id FROM "users" WHERE nome='Mario' AND cognome='Rossi'));
INSERT INTO credentials (id, username, password, ruolo, user_id) VALUES(nextval('credentials_seq'), 'lucia', '$2a$12$tcTCLsq/AeLkNJ3HFYFx7.Js6pN65IEBO.NbkWcGwI.dolo8VOsIi', 'USER', (SELECT id FROM "users" WHERE nome='Lucia' AND cognome='Bianchi'));
INSERT INTO credentials (id, username, password, ruolo, user_id) VALUES(nextval('credentials_seq'), 'direttoreSupremo', '$2a$12$OyEAc2jSNT8g7fPnyZae.u1iSTJhjZn4ACsFV1J9fhdRbeMDIMHWG', 'ADMIN', (SELECT id FROM "users" WHERE nome='Direttore' AND cognome='Supremo'));

-- Inserimento recensioni (usa id utente da tabella user)
INSERT INTO recensione (id, titolo, voto, testo, libro_id, utente_id) SELECT nextval('recensione_seq'), 'Avventura Sottomarina', 5, 'Un classico della fantascienza.', l.id, u.id FROM libro l, "users" u WHERE l.titolo = 'Ventimila Leghe Sotto I Mari' AND u.nome = 'Mario' AND u.cognome = 'Rossi';

INSERT INTO recensione (id, titolo, voto, testo, libro_id, utente_id) SELECT nextval('recensione_seq'), 'Pirati e tesori!', 4, 'Lettura avvincente per ogni età.', l.id, u.id FROM libro l, "users" u WHERE l.titolo = 'L Isola Del Tesoro' AND u.nome = 'Lucia' AND u.cognome = 'Bianchi';
