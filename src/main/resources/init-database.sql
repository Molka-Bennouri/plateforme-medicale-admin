-- Création de la base de données
CREATE DATABASE IF NOT EXISTS plateforme_medicale;
USE plateforme_medicale;

-- Table Utilisateur (Base parent)
CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    type_utilisateur ENUM('medecin', 'patient', 'secretaire', 'admin') NOT NULL,
    role VARCHAR(50),
    actif BOOLEAN DEFAULT TRUE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_type_utilisateur (type_utilisateur)
);

-- Table Specialite
CREATE TABLE specialite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    INDEX idx_nom (nom)
);

-- Table Medecin (hérite de Utilisateur par OneToOne)
CREATE TABLE medecin (
    id INT PRIMARY KEY,
    telephone VARCHAR(20),
    adresse VARCHAR(255) NOT NULL,
    numero_licence VARCHAR(50) UNIQUE NOT NULL,
    specialite_id INT NOT NULL,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    FOREIGN KEY (specialite_id) REFERENCES specialite(id),
    INDEX idx_licence (numero_licence),
    INDEX idx_specialite (specialite_id)
);

-- Table Patient (hérite de Utilisateur par OneToOne)
CREATE TABLE patient (
    id INT PRIMARY KEY,
    date_naissance DATE NOT NULL,
    telephone VARCHAR(20),
    numero_dossier VARCHAR(50) UNIQUE,
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    INDEX idx_dossier (numero_dossier)
);

-- Table Secretaire (hérite de Utilisateur par OneToOne)
CREATE TABLE secretaire (
    id INT PRIMARY KEY,
    bureau VARCHAR(50),
    FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Table Date Disponibilite
CREATE TABLE date_disponibilite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jour DATE NOT NULL UNIQUE,
    mois INT,
    annee INT,
    INDEX idx_jour (jour)
);

-- Table Disponibilite
CREATE TABLE disponibilite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    medecin_id INT NOT NULL,
    date_id INT NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin TIME NOT NULL,
    FOREIGN KEY (medecin_id) REFERENCES medecin(id) ON DELETE CASCADE,
    FOREIGN KEY (date_id) REFERENCES date_disponibilite(id) ON DELETE CASCADE,
    INDEX idx_medecin (medecin_id),
    INDEX idx_date (date_id)
);

-- Table RendezVous
CREATE TABLE rendez_vous (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    medecin_id INT NOT NULL,
    date_heure DATETIME NOT NULL,
    statut ENUM('planifie', 'confirme', 'annule', 'termine') DEFAULT 'planifie',
    est_consultation BOOLEAN DEFAULT FALSE,
    ordonnance TEXT,
    motif TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES medecin(id),
    INDEX idx_patient (patient_id),
    INDEX idx_medecin (medecin_id),
    INDEX idx_date_heure (date_heure),
    INDEX idx_statut (statut)
);

-- Table SecretaireMedecin (association)
CREATE TABLE secretaire_medecin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    secretaire_id INT NOT NULL,
    medecin_id INT NOT NULL,
    date_affectation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (secretaire_id) REFERENCES secretaire(id) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES medecin(id) ON DELETE CASCADE,
    UNIQUE KEY unique_affectation (secretaire_id, medecin_id),
    INDEX idx_secretaire (secretaire_id),
    INDEX idx_medecin (medecin_id)
);

-- Insertion de données de test
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, type_utilisateur, role, actif) VALUES
('Admin', 'System', 'admin@medical.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'admin', 'ADMIN', TRUE);

INSERT INTO specialite (nom, description) VALUES
('Cardiologie', 'Spécialité médicale traitant les maladies du cœur'),
('Dermatologie', 'Spécialité médicale traitant les maladies de la peau'),
('Neurologie', 'Spécialité médicale traitant les maladies du système nerveux'),
('Orthopédie', 'Spécialité médicale traitant les troubles musculosquelettiques');

