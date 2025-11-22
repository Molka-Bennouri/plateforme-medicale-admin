-- Script de peuplement des données de test pour la plateforme médicale

USE plateforme_medicale;

-- Insertion de spécialités (si non déjà présentes)
INSERT IGNORE INTO specialite (nom, description) VALUES
('Cardiologie', 'Spécialité médicale traitant les maladies du cœur'),
('Dermatologie', 'Spécialité médicale traitant les maladies de la peau'),
('Neurologie', 'Spécialité médicale traitant les maladies du système nerveux'),
('Orthopédie', 'Spécialité médicale traitant les troubles musculosquelettiques'),
('Pédiatrie', 'Spécialité médicale traitant les enfants'),
('Gastroentérologie', 'Spécialité médicale traitant les maladies du système digestif');

-- Insertion d'utilisateurs (médecins)
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, type_utilisateur, role, actif) VALUES
('Dupont', 'Jean', 'jean.dupont@medical.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'medecin', 'MEDECIN', TRUE),
('Martin', 'Sophie', 'sophie.martin@medical.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'medecin', 'MEDECIN', TRUE),
('Leclerc', 'Pierre', 'pierre.leclerc@medical.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'medecin', 'MEDECIN', TRUE);

-- Insertion de médecins
INSERT INTO medecin (id, telephone, adresse, numero_licence, specialite_id) VALUES
(2, '0123456789', '123 Rue des Médecins, Paris', 'MED001234', 1),
(3, '0123456790', '456 Avenue de la Santé, Lyon', 'MED001235', 2),
(4, '0123456791', '789 Boulevard Médical, Marseille', 'MED001236', 3);

-- Insertion d'utilisateurs (patients)
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, type_utilisateur, role, actif) VALUES
('Bernard', 'Marie', 'marie.bernard@example.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'patient', 'PATIENT', TRUE),
('Rousseau', 'Luc', 'luc.rousseau@example.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'patient', 'PATIENT', TRUE),
('Petit', 'Anne', 'anne.petit@example.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'patient', 'PATIENT', TRUE),
('Moreau', 'Marc', 'marc.moreau@example.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'patient', 'PATIENT', TRUE);

-- Insertion de patients
INSERT INTO patient (id, date_naissance, telephone, numero_dossier) VALUES
(5, '1990-05-15', '0687654321', 'PAT-00001'),
(6, '1985-03-20', '0687654322', 'PAT-00002'),
(7, '1992-07-10', '0687654323', 'PAT-00003'),
(8, '1988-11-25', '0687654324', 'PAT-00004');

-- Insertion d'utilisateurs (secrétaires)
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, type_utilisateur, role, actif) VALUES
('Gaston', 'Isabelle', 'isabelle.gaston@medical.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'secretaire', 'SECRETAIRE', TRUE),
('Roux', 'Nicole', 'nicole.roux@medical.com', '$2a$10$slYQmyNdGzin7olVN3YO6OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'secretaire', 'SECRETAIRE', TRUE);

-- Insertion de secrétaires
INSERT INTO secretaire (id, bureau) VALUES
(9, 'Bureau 101'),
(10, 'Bureau 102');

-- Insertion de dates de disponibilité
INSERT INTO date_disponibilite (jour, mois, annee) VALUES
('2024-12-20', 12, 2024),
('2024-12-21', 12, 2024),
('2024-12-22', 12, 2024),
('2024-12-23', 12, 2024),
('2024-12-24', 12, 2024),
('2024-12-25', 12, 2024),
('2024-12-26', 12, 2024),
('2024-12-27', 12, 2024);

-- Insertion de disponibilités (médecin 1)
INSERT INTO disponibilite (medecin_id, date_id, heure_debut, heure_fin) VALUES
(2, 1, '09:00:00', '12:00:00'),
(2, 1, '14:00:00', '17:00:00'),
(2, 2, '09:00:00', '12:00:00'),
(2, 2, '14:00:00', '17:00:00');

-- Insertion de disponibilités (médecin 2)
INSERT INTO disponibilite (medecin_id, date_id, heure_debut, heure_fin) VALUES
(3, 1, '10:00:00', '13:00:00'),
(3, 1, '15:00:00', '18:00:00'),
(3, 3, '10:00:00', '13:00:00'),
(3, 3, '15:00:00', '18:00:00');

-- Insertion de disponibilités (médecin 3)
INSERT INTO disponibilite (medecin_id, date_id, heure_debut, heure_fin) VALUES
(4, 2, '08:00:00', '11:00:00'),
(4, 2, '13:00:00', '16:00:00'),
(4, 4, '08:00:00', '11:00:00'),
(4, 4, '13:00:00', '16:00:00');

-- Insertion d'affectations secrétaire-médecin
INSERT INTO secretaire_medecin (secretaire_id, medecin_id, date_affectation) VALUES
(9, 2, NOW()),
(9, 3, NOW()),
(10, 4, NOW());

-- Insertion de rendez-vous
INSERT INTO rendez_vous (patient_id, medecin_id, date_heure, statut, est_consultation, motif, date_creation) VALUES
(5, 2, '2024-12-20 09:30:00', 'confirme', FALSE, 'Consultation cardiologie', NOW()),
(6, 3, '2024-12-21 10:30:00', 'planifie', FALSE, 'Examen dermatologique', NOW()),
(7, 4, '2024-12-22 08:30:00', 'confirme', FALSE, 'Consultation neurologie', NOW()),
(8, 2, '2024-12-23 14:30:00', 'planifie', FALSE, 'Suivi cardiaque', NOW()),
(5, 3, '2024-12-24 15:30:00', 'termine', TRUE, 'Consultation dermatologie', NOW());

-- Mise à jour d'un rendez-vous terminé avec ordonnance
UPDATE rendez_vous SET
    ordonnance = 'Dermocorticoïde - Application locale 2x par jour pendant 7 jours',
    date_modification = NOW()
WHERE id = 5;

-- Affichage du résumé des données
SELECT 'Récapitulatif des données de test' as 'Statut';
SELECT COUNT(*) as 'Nombre de médecins' FROM medecin;
SELECT COUNT(*) as 'Nombre de patients' FROM patient;
SELECT COUNT(*) as 'Nombre de secrétaires' FROM secretaire;
SELECT COUNT(*) as 'Nombre de rendez-vous' FROM rendez_vous;
SELECT COUNT(*) as 'Nombre de disponibilités' FROM disponibilite;

-- Note:
-- Le mot de passe par défaut est 'password123' (haché avec BCrypt)
-- Pour vous connecter en tant que médecin: email: jean.dupont@medical.com
-- Pour vous connecter en tant que patient: email: marie.bernard@example.com
-- Pour vous connecter en tant qu'admin: email: admin@medical.com

