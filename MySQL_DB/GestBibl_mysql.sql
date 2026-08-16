-- =====================================================================
-- GestBibl database — converted from SQL Server (T-SQL) to MySQL
-- Original source: SQL Server Management Studio generated script
-- =====================================================================

-- ---------------------------------------------------------------------
-- NOTE ON SECURITY OBJECTS (not migrated)
-- The original script created SQL Server LOGINS/USERS/ROLES:
--   Users: Employer (login logEmployer), Admin (login logAdmin)
--   Roles: saisie, gestion, administration (+ built-in db_owner etc.)
-- MySQL security is managed differently (CREATE USER / GRANT, no
-- database-scoped "roles" the same way pre-8.0). These are NOT
-- portable 1:1 and are intentionally left out of the schema migration.
-- If you need equivalent MySQL users, create them separately, e.g.:
--   CREATE USER 'employer'@'%' IDENTIFIED BY 'change_me';
--   CREATE USER 'admin'@'%' IDENTIFIED BY 'change_me';
--   GRANT SELECT, INSERT, UPDATE ON GestBibl.* TO 'employer'@'%';
--   GRANT ALL PRIVILEGES ON GestBibl.* TO 'admin'@'%';
-- ---------------------------------------------------------------------

DROP DATABASE IF EXISTS `GestBibl`;
CREATE DATABASE `GestBibl` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `GestBibl`;

-- ---------------------------------------------------------------------
-- TABLES
-- ---------------------------------------------------------------------

CREATE TABLE `EMPLOYE` (
  `id_employe`     INT AUTO_INCREMENT PRIMARY KEY,
  `nom`            VARCHAR(100) NOT NULL,
  `prenom`         VARCHAR(100) NOT NULL,
  `email`          VARCHAR(150) NOT NULL,
  `mot_de_passe`   VARCHAR(255) NOT NULL,
  `role`           VARCHAR(50) NULL,
  `telephone`      VARCHAR(20) NULL,
  `date_embauche`  DATE NOT NULL,
  UNIQUE KEY `uq_employe_email` (`email`),
  CONSTRAINT `chk_employe_role` CHECK (`role` IN ('administration', 'gestion', 'saisie'))
) ENGINE=InnoDB;

CREATE TABLE `SPECIALITE` (
  `id_specialite`   INT AUTO_INCREMENT PRIMARY KEY,
  `nom_specialite`  VARCHAR(100) NOT NULL,
  UNIQUE KEY `uq_specialite_nom` (`nom_specialite`)
) ENGINE=InnoDB;

CREATE TABLE `ETUDIANT` (
  `id_etudiant`      INT AUTO_INCREMENT PRIMARY KEY,
  `nom`              VARCHAR(100) NOT NULL,
  `prenom`           VARCHAR(100) NOT NULL,
  `email`            VARCHAR(150) NOT NULL,
  `id_specialite`    INT NULL,
  `date_inscription` DATE NULL DEFAULT (CURRENT_DATE),
  UNIQUE KEY `uq_etudiant_email` (`email`)
) ENGINE=InnoDB;

CREATE TABLE `MEMOIRE` (
  `id_memoire`     INT AUTO_INCREMENT PRIMARY KEY,
  `titre`          VARCHAR(255) NOT NULL,
  `id_specialite`  INT NOT NULL,
  `annee`          INT NULL,
  `date_ajout`     DATETIME(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  `id_employe`     INT NULL,
  `auteur`         VARCHAR(150) NOT NULL DEFAULT '',
  CONSTRAINT `chk_memoire_annee` CHECK (`annee` >= 2000)
) ENGINE=InnoDB;

CREATE TABLE `EMPRUNT` (
  `id_emprunt`          INT AUTO_INCREMENT PRIMARY KEY,
  `id_memoire`          INT NOT NULL,
  `id_etudiant`         INT NOT NULL,
  `id_employe`          INT NULL,
  `date_emprunt`        DATE NULL DEFAULT (CURRENT_DATE),
  `date_retour_prevue`  DATE NOT NULL,
  `date_retour_reelle`  DATE NULL,
  `statut`              VARCHAR(20) NULL DEFAULT 'Non empru',
  CONSTRAINT `chk_emprunt_statut` CHECK (`statut` IN ('en retard', 'retourné', 'en cours'))
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- DATA
-- ---------------------------------------------------------------------

-- EMPLOYE
INSERT INTO `EMPLOYE` (`id_employe`, `nom`, `prenom`, `email`, `mot_de_passe`, `role`, `telephone`, `date_embauche`) VALUES (1, 'Boufar', 'Ouail', 'ouail@outlook.fr', 'ouail', 'administration', '0775274404', '2001-01-01');
INSERT INTO `EMPLOYE` (`id_employe`, `nom`, `prenom`, `email`, `mot_de_passe`, `role`, `telephone`, `date_embauche`) VALUES (4, 'Belaatar', 'Mouhamed', 'khalil@gmail.com', 'khalil', 'administration', '0660', '1996-12-01');
INSERT INTO `EMPLOYE` (`id_employe`, `nom`, `prenom`, `email`, `mot_de_passe`, `role`, `telephone`, `date_embauche`) VALUES (5, 'T', 'T', 'T', 'T', 'gestion', '0', '1999-09-09');

-- SPECIALITE
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (2, 'Automatisme');
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (1, 'Base de Données');
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (4, 'Electronique');
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (7, 'GRH');
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (3, 'Maintenance');
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (5, 'Réseau');
INSERT INTO `SPECIALITE` (`id_specialite`, `nom_specialite`) VALUES (6, 'Télécommunication');

-- ETUDIANT
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (1, 'Dormane', 'Islem', 'islem@gmail.com', 1, '2026-05-01');
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (2, 'Kebbab', 'Anes', 'anes', 1, '2026-04-27');
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (3, 'Fetha', 'Mouhamed', 'mouhamed', 2, '2026-02-10');
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (5, 'Boufar', 'ouail', 'ouail', 1, '2026-05-03');
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (6, 'Zergui', 'Fouad', 'Fouad@gmail.cil', 1, '2026-05-04');
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (7, 'HAJAJ', 'BRAHIM', 'HAJ', 1, '2026-05-07');
INSERT INTO `ETUDIANT` (`id_etudiant`, `nom`, `prenom`, `email`, `id_specialite`, `date_inscription`) VALUES (10, 'boukhari', 'zinou', 'zino', 7, '2026-07-31');

-- MEMOIRE
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (3, 'Conception et réalisation d''une application de gestion de stock.', 1, 2025, '2027-02-09 00:00:00.000', 1, 'Bouaalem');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (4, 'Conception d''une plateforme d''apprentissage en ligne ', 1, 2024, '2026-05-03 20:43:59.790', NULL, 'Rebai Dounya');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (5, 'Mémoire sur les réseaux informatiques ', 5, 2024, '2026-05-03 20:45:13.230', NULL, 'Raid Khaila');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (6, 'Évaluation de la sécurité réseau, étude de cas EPS Skikda.', 5, 2022, '2026-05-03 20:45:50.007', NULL, 'Taguigue');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (7, 'Étude sur le "Pont Gratteur" en automatisme', 2, 2023, '2026-05-03 20:46:36.430', NULL, 'Mohamed Amine Fernie, Latoui Chaïma, Lounis Djihane et Boudjelal Assala');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (8, 'Travail sur la commande d''une machine empaqueteuse via automate Siemens', 4, 2022, '2026-05-03 20:47:48.523', NULL, 'Lounis Djihane');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (9, 'Travail sur l''automatisation des préinscriptions ', 4, 2023, '2026-05-03 20:48:41.063', NULL, 'Seghighen Larbi et Aggoune Khaled');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (10, 'Système d''information pour la gestion des déchets urbains (Projet technique).', 1, 2018, '2026-05-03 20:49:33.857', NULL, 'Daoud Mohamed Amine, Adda Chahrazed, Bouchama Sanaa et Boumaza Chaimaa');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (11, 'Analyse de la dynamique d''innovation, citant le rôle de l''INSFP dans le système national d''innovation', 7, 2024, '2026-05-03 20:50:16.513', NULL, 'Maghni Billal');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (16, 'Gestion de scolarité', 1, 2012, '2026-05-07 08:39:19.237', NULL, 'Talbi Sarraa ');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (17, 'Réalisation d''un système d''information pour la facturation du produit pharmaceutique', 1, 2012, '2026-05-07 08:40:47.743', NULL, 'Bramki Chafik');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (19, 'Conception et implémentation d''une base de données avec merise et hyper file SQL serveur', 1, 2013, '2026-05-07 08:42:31.710', NULL, 'Belaid Chahrazed');
INSERT INTO `MEMOIRE` (`id_memoire`, `titre`, `id_specialite`, `annee`, `date_ajout`, `id_employe`, `auteur`) VALUES (20, 'Suivi de traitement des factures', 1, 2013, '2026-05-07 08:51:58.767', NULL, 'Tir Lamia');

-- EMPRUNT
INSERT INTO `EMPRUNT` (`id_emprunt`, `id_memoire`, `id_etudiant`, `id_employe`, `date_emprunt`, `date_retour_prevue`, `date_retour_reelle`, `statut`) VALUES (6, 4, 2, NULL, '2026-05-07', '2026-05-17', '2026-07-30', 'retourné');
INSERT INTO `EMPRUNT` (`id_emprunt`, `id_memoire`, `id_etudiant`, `id_employe`, `date_emprunt`, `date_retour_prevue`, `date_retour_reelle`, `statut`) VALUES (7, 4, 5, NULL, '2026-05-07', '2026-05-17', NULL, 'en cours');
INSERT INTO `EMPRUNT` (`id_emprunt`, `id_memoire`, `id_etudiant`, `id_employe`, `date_emprunt`, `date_retour_prevue`, `date_retour_reelle`, `statut`) VALUES (8, 19, 7, NULL, '2026-07-30', '2026-08-09', NULL, 'en cours');
INSERT INTO `EMPRUNT` (`id_emprunt`, `id_memoire`, `id_etudiant`, `id_employe`, `date_emprunt`, `date_retour_prevue`, `date_retour_reelle`, `statut`) VALUES (9, 4, 5, NULL, '2026-08-08', '2026-08-09', NULL, 'en cours');

-- Reset AUTO_INCREMENT counters to continue after the highest explicit id used above
ALTER TABLE `EMPLOYE` AUTO_INCREMENT = 6;
ALTER TABLE `SPECIALITE` AUTO_INCREMENT = 8;
ALTER TABLE `ETUDIANT` AUTO_INCREMENT = 11;
ALTER TABLE `MEMOIRE` AUTO_INCREMENT = 21;
ALTER TABLE `EMPRUNT` AUTO_INCREMENT = 10;

-- ---------------------------------------------------------------------
-- FOREIGN KEYS (added after data load, mirrors original script order)
-- ---------------------------------------------------------------------

ALTER TABLE `EMPRUNT`
  ADD CONSTRAINT `fk_emprunt_employe`
  FOREIGN KEY (`id_employe`) REFERENCES `EMPLOYE` (`id_employe`);

ALTER TABLE `EMPRUNT`
  ADD CONSTRAINT `fk_emprunt_etudiant`
  FOREIGN KEY (`id_etudiant`) REFERENCES `ETUDIANT` (`id_etudiant`);

ALTER TABLE `EMPRUNT`
  ADD CONSTRAINT `fk_emprunt_memoire`
  FOREIGN KEY (`id_memoire`) REFERENCES `MEMOIRE` (`id_memoire`)
  ON DELETE CASCADE;

ALTER TABLE `ETUDIANT`
  ADD CONSTRAINT `fk_etudiant_specialite`
  FOREIGN KEY (`id_specialite`) REFERENCES `SPECIALITE` (`id_specialite`)
  ON DELETE SET NULL;

ALTER TABLE `MEMOIRE`
  ADD CONSTRAINT `fk_memoire_employe`
  FOREIGN KEY (`id_employe`) REFERENCES `EMPLOYE` (`id_employe`)
  ON DELETE SET NULL;

ALTER TABLE `MEMOIRE`
  ADD CONSTRAINT `fk_memoire_specialite`
  FOREIGN KEY (`id_specialite`) REFERENCES `SPECIALITE` (`id_specialite`);
