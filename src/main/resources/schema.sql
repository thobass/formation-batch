--
-- Base de données :  formations-db
--

-- --------------------------------------------------------
DROP TABLE IF EXISTS seances;
DROP TABLE IF EXISTS formations;
DROP TABLE IF EXISTS formateurs;



CREATE TABLE formateurs (
                            id int NOT NULL,
                            nom varchar(12) NOT NULL,
                            prenom varchar(12) NOT NULL,
                            adresse_email varchar(50) NOT NULL
);

CREATE TABLE formations (
                            code varchar(20) NOT NULL,
                            libelle varchar(200) NOT NULL,
                            descriptif varchar(500) NOT NULL
);

CREATE TABLE seances (
                         code_formation varchar(20) NOT NULL,
                         id_formateur int NOT NULL,
                         date_debut date NOT NULL,
                         date_fin date NOT NULL
);


ALTER TABLE formateurs
    ADD PRIMARY KEY (id);

ALTER TABLE formations
    ADD PRIMARY KEY (code);

-- ALTER TABLE seances
--    ADD PRIMARY KEY (code_formation,id_formateur);


ALTER TABLE seances
    ADD CONSTRAINT FK_FORMATEURS FOREIGN KEY (id_formateur) REFERENCES formateurs (id);
ALTER TABLE seances
    ADD CONSTRAINT FK_FORMATIONS FOREIGN KEY (code_formation) REFERENCES formations (code);
