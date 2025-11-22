# 🔌 Quick API Reference - Plateforme Médicale

## 📌 Base URL
```
http://localhost:8080/api
```

## 🔐 Authentification

### Connexion (Login)
```http
POST /auth/login
Content-Type: application/json

{
  "email": "medecin@example.com",
  "motDePasse": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "medecin@example.com",
  "nom": "Dupont",
  "prenom": "Jean",
  "typeUtilisateur": "medecin"
}
```

### Validation Token
```http
GET /auth/validate/{token}
Authorization: Bearer {token}

Response: 200 OK
"Token valide pour : medecin@example.com"
```

---

## 👤 Utilisateurs

### Lister tous
```http
GET /utilisateurs
Authorization: Bearer {token}

Response: 200 OK
[{ id, nom, prenom, email, type_utilisateur, actif, ... }]
```

### Lister actifs
```http
GET /utilisateurs/actifs
Authorization: Bearer {token}
```

### Par ID
```http
GET /utilisateurs/{id}
```

### Par Email
```http
GET /utilisateurs/email/{email}
```

### Par Type
```http
GET /utilisateurs/type/{type}
Types: medecin, patient, secretaire, admin
```

### Recherche
```http
GET /utilisateurs/search/{nom}
```

### Créer
```http
POST /utilisateurs
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean@example.com",
  "motDePasse": "password123",
  "typeUtilisateur": "medecin"
}
```

### Modifier
```http
PUT /utilisateurs/{id}
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean-Pierre",
  "email": "jean.pierre@example.com"
}
```

### Supprimer
```http
DELETE /utilisateurs/{id}
```

### Activer
```http
POST /utilisateurs/{id}/activer
```

### Désactiver
```http
POST /utilisateurs/{id}/desactiver
```

---

## 👨‍⚕️ Médecins

### Lister
```http
GET /medecins
```

### Actifs
```http
GET /medecins/actifs
```

### Par Spécialité
```http
GET /medecins/specialite/{specialiteId}
```

### Par Licence
```http
GET /medecins/licence/{numeroLicence}
```

### Créer Médecin
```http
POST /medecins
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@medical.com",
  "motDePasse": "password123",
  "telephone": "0123456789",
  "adresse": "123 Rue des Médecins",
  "numeroLicence": "MED123456",
  "specialiteId": 1
}
```

### Modifier
```http
PUT /medecins/{id}
```

### Arrêter
```http
POST /medecins/{id}/arreter
```

---

## 🏥 Patients

### Lister
```http
GET /patients
```

### Actifs
```http
GET /patients/actifs
```

### Par Dossier
```http
GET /patients/dossier/{numeroDossier}
```

### Créer Patient
```http
POST /patients
Content-Type: application/json

{
  "nom": "Martin",
  "prenom": "Sophie",
  "email": "sophie.martin@example.com",
  "motDePasse": "password123",
  "dateNaissance": "1990-05-15",
  "telephone": "0987654321"
}
```

---

## 📅 Rendez-vous

### Lister
```http
GET /rendez-vous
```

### Par Médecin
```http
GET /rendez-vous/medecin/{medecinId}
```

### Par Patient
```http
GET /rendez-vous/patient/{patientId}
```

### À venir (Patient)
```http
GET /rendez-vous/patient/{patientId}/avenir
```

### Par Statut
```http
GET /rendez-vous/statut/{statut}
Statuts: planifie, confirme, annule, termine
```

### Créer
```http
POST /rendez-vous
Content-Type: application/json

{
  "patientId": 1,
  "medecinId": 1,
  "dateHeure": "2024-12-25T14:30:00",
  "motif": "Consultation générale"
}
```

### Modifier
```http
PUT /rendez-vous/{id}
Content-Type: application/json

{
  "dateHeure": "2024-12-26T10:00:00",
  "motif": "Consultation"
}
```

### Confirmer
```http
POST /rendez-vous/{id}/confirmer
```

### Annuler
```http
POST /rendez-vous/{id}/annuler
```

### Terminer (Médecin)
```http
POST /rendez-vous/{id}/terminer?ordonnance=Amoxicilline 500mg
```

---

## 📊 Statistiques

### Statistiques Générales
```http
GET /statistiques/general

Response: {
  "totalMedecins": 5,
  "totalSecretaires": 3,
  "totalPatients": 50,
  "totalRendezVous": 120,
  "rendezVousParStatut": { "planifie": 30, ... },
  "consultationsTerminees": 100,
  "tauxCompletude": "83.33%"
}
```

### Stats Médecins
```http
GET /statistiques/medecins

Response: [
  {
    "medecinId": 1,
    "medecinNom": "Dupont Jean",
    "specialite": "Cardiologie",
    "totalRendezVous": 20,
    "rendezVousTermines": 18,
    "tauxCompletude": "90%"
  }
]
```

### Évaluer Médecin
```http
GET /statistiques/medecin/{medecinId}/evaluation

Response: {
  "totalRendezVous": 20,
  "rendezVousTermines": 18,
  "tauxCompletude": "90%",
  "tauxAnnulation": "5%"
}
```

### Rendez-vous à Venir
```http
GET /statistiques/rendez-vous/avenir
```

### RDV par Spécialité
```http
GET /statistiques/rendez-vous/par-specialite

Response: {
  "Cardiologie": 25,
  "Dermatologie": 15,
  "Neurologie": 20
}
```

### Taux Confirmation
```http
GET /statistiques/confirmation/taux

Response: {
  "tauxConfirmation": "85.50%",
  "confirmesCount": 102,
  "totalCount": 119
}
```

---

## 🏥 Spécialités

### Lister
```http
GET /specialites
```

### Par Nom
```http
GET /specialites/nom/{nom}
```

### Recherche
```http
GET /specialites/search/{nom}
```

### Créer
```http
POST /specialites
Content-Type: application/json

{
  "nom": "Cardiologie",
  "description": "Spécialité médicale traitant les maladies du cœur"
}
```

### Modifier
```http
PUT /specialites/{id}
Content-Type: application/json

{
  "nom": "Cardiologie",
  "description": "Description mise à jour"
}
```

---

## 👷 Secrétaires

### Lister
```http
GET /secretaires
```

### Actifs
```http
GET /secretaires/actifs
```

### Recherche
```http
GET /secretaires/search/{nom}
```

### Créer
```http
POST /secretaires
Content-Type: application/json

{
  "nom": "Martin",
  "prenom": "Claire",
  "email": "claire.martin@example.com",
  "bureau": "Bureau 101"
}
```

---

## 📋 Disponibilités

### Par Médecin
```http
GET /disponibilites/medecin/{medecinId}
```

### Médecin + Date
```http
GET /disponibilites/medecin/{medecinId}/date/{date}
Format date: 2024-12-25
```

### Par Date
```http
GET /disponibilites/date/{date}
```

### Créer
```http
POST /disponibilites
Content-Type: application/json

{
  "medecinId": 1,
  "date": "2024-12-25",
  "heureDebut": "09:00",
  "heureFin": "17:00"
}
```

### Modifier
```http
PUT /disponibilites/{id}
Content-Type: application/json

{
  "heureDebut": "08:00",
  "heureFin": "16:00"
}
```

---

## 🔗 Affectations Secrétaires-Médecins

### Lister
```http
GET /secretaires-medecins
```

### Par Médecin
```http
GET /secretaires-medecins/medecin/{medecinId}
```

### Secrétaires Actifs d'un Médecin
```http
GET /secretaires-medecins/medecin/{medecinId}/actifs
```

### Affecter
```http
POST /secretaires-medecins?medecinId=1&secretaireId=2
```

### Désaffecter
```http
DELETE /secretaires-medecins/medecin/{medecinId}/secretaire/{secretaireId}
```

---

## 📝 Headers Requis

```http
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}
```

## 🔴 Status Codes

| Code | Signification |
|------|---------------|
| 200 | OK - Succès |
| 201 | Created - Ressource créée |
| 204 | No Content - Suppressión réussie |
| 400 | Bad Request - Requête invalide |
| 401 | Unauthorized - Authentication requise |
| 403 | Forbidden - Accès refusé |
| 404 | Not Found - Ressource non trouvée |
| 409 | Conflict - Doublon détecté |
| 500 | Internal Server Error - Erreur serveur |

## 🧪 Données de Test

### Admin par défaut
```
Email: admin@medical.com
Mot de passe: hashedPassword (voir init-database.sql)
```

### Spécialités
1. Cardiologie
2. Dermatologie
3. Neurologie
4. Orthopédie

---

**Dernière mise à jour:** 2024-12-22
**Version API:** 1.0.0

