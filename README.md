# Plateforme Médicale - Documentation Complète

## 📋 Vue d'ensemble

La **Plateforme Médicale** est une application Spring Boot modulaire conçue pour gérer les rendez-vous médicaux, les disponibilités des médecins, et les utilisateurs (patients, médecins, secrétaires).

## 🏗️ Architecture

### Modules implémentés

1. **Authentification et Autorisation**
   - JWT Token-based authentication
   - Gestion des utilisateurs avec roles (ADMIN, MEDECIN, PATIENT, SECRETAIRE)
   - Activation/Désactivation des comptes

2. **Gestion des Utilisateurs**
   - Création, lecture, modification, suppression (CRUD)
   - Recherche par critères
   - Gestion des mots de passe (hachage BCrypt)

3. **Gestion des Médecins**
   - Création de profils de médecins
   - Assignation de spécialités
   - Numéro de licence unique
   - Arrêt/Activation des médecins

4. **Gestion des Patients**
   - Création de dossiers patients
   - Numéro de dossier auto-généré
   - Historique de rendez-vous

5. **Gestion des Secrétaires**
   - Affectation des secrétaires aux médecins
   - Gestion des bureaux
   - Support multi-secrétaires par médecin

6. **Gestion des Disponibilités**
   - Création de créneaux horaires pour médecins
   - Vérification des conflits
   - Filtrage par date et médecin

7. **Gestion des Rendez-vous**
   - Réservation de rendez-vous
   - Gestion des statuts (planifié, confirmé, annulé, terminé)
   - Génération d'ordonnances
   - Historique des consultations

8. **Statistiques et Rapports**
   - Statistiques globales (totaux, taux de confirmation)
   - Statistiques par médecin (rendez-vous, taux de complétude)
   - Rendez-vous à venir
   - Distribution par spécialité

## 🛠️ Technologie Utilisée

- **Framework**: Spring Boot 3.5.8
- **Base de données**: MySQL 8.0
- **Authentification**: JWT (JSON Web Tokens)
- **Validation**: Jakarta Validation
- **ORM**: JPA/Hibernate
- **Build**: Maven
- **Java**: 21

## 📦 Dépendances Principales

```xml
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-web
- spring-boot-starter-validation
- jjwt (JWT Library)
- lombok
- mysql-connector-j
- springdoc-openapi (Swagger/OpenAPI)
```

## 🚀 Installation et Configuration

### 1. Clonage du projet
```bash
git clone <repository-url>
cd medical-platform
```

### 2. Configuration de la base de données

Créez une base de données MySQL et exécutez le script SQL :

```bash
mysql -u root -p < src/main/resources/init-database.sql
```

### 3. Configuration application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/plateforme_medicale?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe

jwt.secret=MyVeryLongSecretKeyForJWTTokenGenerationThatIsAtLeast256bits
jwt.expiration=86400000
```

### 4. Build et lancement

```bash
mvn clean install
mvn spring-boot:run
```

L'application sera disponible sur `http://localhost:8080`

## 📚 API Endpoints

### 🔐 Authentification
- `POST /api/auth/login` - Connexion utilisateur
- `GET /api/auth/validate/{token}` - Validation du token JWT

### 👤 Utilisateurs
- `GET /api/utilisateurs` - Récupérer tous les utilisateurs
- `GET /api/utilisateurs/{id}` - Récupérer un utilisateur
- `GET /api/utilisateurs/email/{email}` - Rechercher par email
- `GET /api/utilisateurs/type/{type}` - Récupérer par type
- `POST /api/utilisateurs` - Créer un utilisateur
- `PUT /api/utilisateurs/{id}` - Modifier un utilisateur
- `DELETE /api/utilisateurs/{id}` - Supprimer un utilisateur
- `POST /api/utilisateurs/{id}/activer` - Activer un utilisateur
- `POST /api/utilisateurs/{id}/desactiver` - Désactiver un utilisateur

### 👨‍⚕️ Médecins
- `GET /api/medecins` - Lister tous les médecins
- `GET /api/medecins/{id}` - Détails d'un médecin
- `GET /api/medecins/specialite/{specialiteId}` - Médecins par spécialité
- `GET /api/medecins/licence/{numeroLicence}` - Rechercher par licence
- `POST /api/medecins` - Créer un médecin
- `PUT /api/medecins/{id}` - Modifier un médecin
- `DELETE /api/medecins/{id}` - Supprimer un médecin
- `POST /api/medecins/{id}/arreter` - Arrêter un médecin

### 🏥 Patients
- `GET /api/patients` - Lister tous les patients
- `GET /api/patients/{id}` - Détails d'un patient
- `GET /api/patients/dossier/{numeroDossier}` - Rechercher par dossier
- `POST /api/patients` - Créer un patient
- `PUT /api/patients/{id}` - Modifier un patient
- `DELETE /api/patients/{id}` - Supprimer un patient

### 📅 Rendez-vous
- `GET /api/rendez-vous` - Lister les rendez-vous
- `GET /api/rendez-vous/{id}` - Détails d'un rendez-vous
- `GET /api/rendez-vous/medecin/{medecinId}` - RDV d'un médecin
- `GET /api/rendez-vous/patient/{patientId}` - RDV d'un patient
- `GET /api/rendez-vous/statut/{statut}` - RDV par statut
- `POST /api/rendez-vous` - Créer un rendez-vous
- `PUT /api/rendez-vous/{id}` - Modifier un rendez-vous
- `POST /api/rendez-vous/{id}/confirmer` - Confirmer un RDV
- `POST /api/rendez-vous/{id}/annuler` - Annuler un RDV
- `POST /api/rendez-vous/{id}/terminer` - Terminer un RDV

### 📊 Statistiques
- `GET /api/statistiques/general` - Stats générales
- `GET /api/statistiques/medecins` - Stats par médecin
- `GET /api/statistiques/medecin/{id}/evaluation` - Évaluation médecin
- `GET /api/statistiques/rendez-vous/avenir` - RDV à venir
- `GET /api/statistiques/rendez-vous/par-specialite` - RDV par spécialité
- `GET /api/statistiques/confirmation/taux` - Taux de confirmation

## 📄 Modèle de Données

### Entités Principales

**Utilisateur**
- id, nom, prenom, email, mot_de_passe
- type_utilisateur, role, actif
- date_creation, date_modification

**Medecin** (OneToOne avec Utilisateur)
- id, utilisateur, telephone, adresse
- numero_licence, specialite

**Patient** (OneToOne avec Utilisateur)
- id, utilisateur, date_naissance, telephone
- numero_dossier

**Secretaire** (OneToOne avec Utilisateur)
- id, utilisateur, bureau

**RendezVous**
- id, patient, medecin, date_heure
- statut, est_consultation, ordonnance, motif

**Disponibilite**
- id, medecin, date, heure_debut, heure_fin

**SecretaireMedecin** (Association Many-to-Many)
- id, secretaire, medecin, date_affectation

**Specialite**
- id, nom, description

## 🔒 Sécurité

### Authentification
- Les mots de passe sont hachés avec BCrypt
- JWT tokens avec expiration de 24h
- Validation des tokens sur les endpoints protégés

### Validation
- Validation côté serveur avec Jakarta Validation
- Vérification des doublons (email, licence médicale, etc.)

### Base de données
- Contraintes UNIQUE sur les champs sensibles
- Foreign Keys pour l'intégrité référentielle
- Suppression en cascade configurée

## 📖 Documentation API

Swagger UI est accessible sur : `http://localhost:8080/swagger-ui.html`

## 🧪 Exemple de Requêtes

### 1. Connexion
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "medecin@example.com",
    "motDePasse": "password123"
  }'
```

### 2. Créer un médecin
```bash
curl -X POST http://localhost:8080/api/medecins \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@medical.com",
    "motDePasse": "password123",
    "telephone": "0123456789",
    "adresse": "123 rue des Médecins",
    "numeroLicence": "MED123456",
    "specialiteId": 1
  }'
```

### 3. Créer un rendez-vous
```bash
curl -X POST http://localhost:8080/api/rendez-vous \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "patientId": 1,
    "medecinId": 1,
    "dateHeure": "2024-12-25T14:30:00",
    "motif": "Consultation générale"
  }'
```

## 🛟 Dépannage

### Erreur de connexion à la base de données
- Vérifiez que MySQL est en cours d'exécution
- Vérifiez les identifiants dans application.properties
- Assurez-vous que la base de données existe

### Erreur 401 Unauthorized
- Vérifiez que le token JWT est envoyé dans le header `Authorization: Bearer <token>`
- Vérifiez que le token n'a pas expiré

### Erreur de validation
- Vérifiez que tous les champs obligatoires sont fournis
- Vérifiez que le format des données est correct (email, date, etc.)

## 📝 Licence

MIT License - Voir LICENSE pour plus de détails

## 👥 Contributeurs

Équipe de développement - Plateforme Médicale

## 📞 Support

Pour les questions ou problèmes, veuillez contacter : support@medical-platform.com

