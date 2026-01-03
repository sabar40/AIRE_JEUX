# AIRE_JEUX - API de Gestion des Jeux et Réservations

API REST pour la gestion d'une aire de jeux permettant aux utilisateurs de consulter les équipements disponibles et de créer des réservations.

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Utilisation](#-utilisation)
- [API Endpoints](#-api-endpoints)
- [Gestion des erreurs](#-gestion-des-erreurs)
- [Tests](#-tests)
- [Structure du projet](#-structure-du-projet)
- [Sécurité](#-sécurité)
- [Contributeurs](#-contributeurs)

## ✨ Fonctionnalités

### 🔐 Authentification & Autorisation
- ✅ Inscription et connexion des utilisateurs
- ✅ Authentification JWT (JSON Web Token)
- ✅ Gestion des rôles (`USER`, `ADMIN`)
- ✅ Sécurisation des endpoints avec Spring Security

### 🎯 Gestion des Jeux
- ✅ CRUD complet des équipements de jeux
- ✅ Localisation GPS (coordonnées géographiques)
- ✅ Accès public en lecture, modification réservée aux admins

### 📅 Système de Réservations
- ✅ Création de réservations par les utilisateurs
- ✅ Workflow de validation : `PENDING` → `APPROVED` / `REJECTED` / `CANCELLED`
- ✅ Gestion des conflits (impossible de supprimer un jeu avec réservations actives)
- ✅ Historique des réservations par utilisateur

### 🛡️ Gestion des Exceptions
- ✅ Handler global centralisé (`@RestControllerAdvice`)
- ✅ Réponses d'erreur standardisées (format JSON uniforme)
- ✅ Codes HTTP appropriés (404, 401, 403, 409, 500, etc.)
- ✅ Logging des erreurs avec SLF4J

## Architecture

\`\`\`
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP/JSON + JWT
       ▼
┌─────────────────────────────────────┐
│         Controllers                  │
│  (AuthController, JeuxController,   │
│   ReservationController, etc.)      │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│      Security Layer                  │
│  (JwtFilter, SecurityConfig)        │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         Services                     │
│  (JeuxService, ReservationService,  │
│   UtilisateurService)               │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│       Repositories (JPA)            │
│  (JeuxRepository, etc.)             │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│      MariaDB Database               │
│  (Tables: utilisateur, jeux,        │
│   coordonnees, reservation)         │
└─────────────────────────────────────┘
\`\`\`

### Pattern : Clean Architecture
- **Controllers** : Gestion des requêtes HTTP
- **Services** : Logique métier
- **Repositories** : Accès aux données
- **DTOs** : Transfert de données entre couches
- **Mappers** : Conversion Entity ↔ DTO (MapStruct)

## 🛠️ Technologies

### Backend
- **Java 17** - Langage de programmation
- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Authentification et autorisation
- **Spring Data JPA** - ORM et accès aux données
- **Hibernate** - Implémentation JPA

### Sécurité
- **JWT (jjwt 0.11.5)** - Tokens d'authentification
- **BCrypt** - Hachage des mots de passe

### Base de données
- **MariaDB** - SGBD relationnel
- **HikariCP** - Pool de connexions

### Mapping & Validation
- **MapStruct 1.5.5** - Mapping automatique Entity/DTO
- **Lombok** - Réduction du boilerplate

### Build & Tests
- **Maven** - Gestion des dépendances
- **JUnit 5** - Tests unitaires
- **Mockito** - Framework de mocking

## 📦 Prérequis

- **Java Development Kit (JDK) 17+**
- **Maven 3.6+**
- **MariaDB 10.5+** (ou MySQL 8.0+)
- **Git**

## 🚀 Installation

### 1. Cloner le repository

\`\`\`bash
git clone https://github.com/sabar40/AIRE_JEUX.git
cd AIRE_JEUX/airejeux
\`\`\`

### 2. Configurer la base de données

Cette étape prépare la base de données pour l'application.

**a. Se connecter à MariaDB**

Connectez-vous à votre instance MariaDB avec un utilisateur ayant les droits de création de base de données (par exemple, `root`).

```bash
sudo mysql -u root -p
```

**b. Créer la base de données**

Une fois connecté, exécutez la commande suivante pour créer la base de données, puis quittez le client `mysql`.

```sql
CREATE DATABASE AireJeux CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

**c. Importer le schéma**

Après avoir créé la base de données, importez sa structure en exécutant cette commande depuis votre terminal (pas dans le client `mysql`).

```bash
mysql -u root -p AireJeux < src/main/resources/airejeux_structure_complete.sql
```
### 4. Compiler et lancer l'application

\`\`\`bash
# Compiler le projet
./mvnw clean compile

# Lancer l'application
./mvnw spring-boot:run
\`\`\`

L'API sera accessible sur : **http://localhost:8080**


## 📖 Utilisation

### Workflow typique

1. **S'inscrire** (POST `/api/auth/register`)
2. **Se connecter** (POST `/api/auth/login`) → Récupérer le JWT
3. **Consulter les jeux** (GET `/api/jeux`) - Public
4. **Créer une réservation** (POST `/api/reservations`) - Authentifié
5. **Admin approuve** (PUT `/api/reservations/{id}/status`) - Admin uniquement

### Exemple avec curl

```bash
# 1. Inscription
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123",
    "nom": "Doe",
    "prenom": "John",
    "mail": "john@example.com",
    "role": "USER"
  }'

# 2. Connexion (récupérer le token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123"
  }')

# 3. Consulter les jeux
curl -X GET http://localhost:8080/api/jeux \
  -H "Authorization: Bearer $TOKEN"

# 4. Créer une réservation
curl -X POST http://localhost:8080/api/reservations \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "jeuxId": 1,
    "utilisateurId": 1,
    "dateDebut": "2025-12-01T10:00:00",
    "dateFin": "2025-12-01T12:00:00"
  }'
```

## 🌐 API Endpoints

### 🔐 Authentification

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/auth/register` | Inscription d'un utilisateur | Public |
| POST | `/api/auth/login` | Connexion (retourne JWT) | Public |

### 🎮 Jeux

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/jeux` | Liste tous les jeux | Public |
| GET | `/api/jeux/{id}` | Récupère un jeu par ID | Public |
| POST | `/api/jeux` | Crée un nouveau jeu | ADMIN |
| PUT | `/api/jeux/{id}` | Modifie un jeu | ADMIN |
| DELETE | `/api/jeux/{id}` | Supprime un jeu | ADMIN |

### 📅 Réservations

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/reservations` | Liste toutes les réservations | ADMIN |
| GET | `/api/reservations/{id}` | Détails d'une réservation | USER |
| GET | `/api/reservations/user/{userId}` | Réservations d'un utilisateur | USER |
| POST | `/api/reservations` | Crée une réservation | USER |
| PUT | `/api/reservations/{id}/status` | Change le statut | ADMIN |
| DELETE | `/api/reservations/{id}` | Annule une réservation | USER/ADMIN |

### 👤 Utilisateurs

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/users` | Liste tous les utilisateurs | ADMIN |
| GET | `/api/users/{id}` | Récupère un utilisateur | USER |
| PUT | `/api/users/{id}` | Modifie un utilisateur | USER/ADMIN |
| DELETE | `/api/users/{id}` | Supprime un utilisateur | ADMIN |

## 🚨 Gestion des erreurs

L'API utilise un système centralisé de gestion des exceptions avec des réponses standardisées.

### Format de réponse d'erreur

```json
{
  "timestamp": "2025-11-18T12:53:36",
  "status": 404,
  "error": "Not Found",
  "message": "Jeux avec id '999' introuvable",
  "path": "/api/jeux/999",
  "validationErrors": []
}
```

### Codes HTTP

| Code | Signification | Exemple |
|------|--------------|---------|
| 200 | Succès | Opération réussie |
| 400 | Bad Request | Données invalides |
| 401 | Unauthorized | Token manquant/invalide |
| 403 | Forbidden | Permissions insuffisantes |
| 404 | Not Found | Ressource introuvable |
| 409 | Conflict | Conflit métier (duplication, etc.) |
| 500 | Internal Server Error | Erreur serveur |

### Exceptions personnalisées

- `ResourceNotFoundException` (404)
- `BadRequestException` (400)
- `UnauthorizedException` (401)
- `ForbiddenException` (403)
- `ConflictException` (409)
- `DuplicateResourceException` (409)
- `InvalidTokenException` (401)
- `ReservationException` (400)

## 🧪 Tests

### Tests unitaires

```bash
# Exécuter tous les tests
./mvnw test

# Exécuter les tests d'un fichier spécifique
./mvnw test -Dtest=GlobalExceptionHandlerTest

# Tests avec couverture
./mvnw test jacoco:report
```

### Tests d'intégration (avec BD)

```bash
# Script de tests curl
chmod +x test-exceptions.sh
./test-exceptions.sh
```

### Résultats attendus

```
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS ✅
```

## 📁 Structure du projet

```
airejeux/
├── src/
│   ├── main/
│   │   ├── java/projet/polytech/airejeux/
│   │   │   ├── config/              # Configuration (SecurityConfig)
│   │   │   ├── controller/          # Contrôleurs REST
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── Entity/              # Entités JPA
│   │   │   ├── exception/           # Exceptions personnalisées
│   │   │   ├── mapper/              # Mappers MapStruct
│   │   │   ├── Repository/          # Interfaces JPA
│   │   │   ├── security/            # JWT Filter
│   │   │   ├── Service/             # Logique métier
│   │   │   ├── utils/               # Classes utilitaires
│   │   │   └── AirejeuxApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── airejeux_structure_complete.sql
│   └── test/
│       └── java/projet/polytech/airejeux/
│           ├── exception/           # Tests des exceptions
│           └── AirejeuxApplicationTests.java
├── target/                          # Build artifacts
├── .gitignore
├── pom.xml                          # Configuration Maven
├── README.md
└── test-exceptions.sh               # Script de tests curl
```

## 🔒 Sécurité

### Bonnes pratiques implémentées

✅ Hachage BCrypt des mots de passe  
✅ Authentification JWT stateless  
✅ Validation des entrées utilisateur  
✅ Gestion des rôles (RBAC)  
✅ Protection CSRF désactivée (API REST)  
✅ Gestion centralisée des exceptions  

## 📊 Modèle de données

### Entités principales

```
Utilisateur (utilisateur)
├── id: Long (PK)
├── username: String (unique)
├── password: String (hashed)
├── nom: String
├── prenom: String
├── mail: String
└── role: String (USER/ADMIN)

Jeux (jeux)
├── id: Long (PK)
├── nom: String
├── type: String
├── description: String
└── coordonnees_id: Long (FK → Coordonnees)

Coordonnees (coordonnees)
├── id: Long (PK)
├── latitude: Double
└── longitude: Double

Reservation (reservation)
├── id: Long (PK)
├── jeux_id: Long (FK → Jeux)
├── utilisateur_id: Long (FK → Utilisateur)
├── dateDebut: DateTime
├── dateFin: DateTime
└── status: String (PENDING/APPROVED/REJECTED/CANCELLED)
```

### Relations

- `Jeux` ↔ `Coordonnees` : OneToOne
- `Reservation` → `Jeux` : ManyToOne
- `Reservation` → `Utilisateur` : ManyToOne

## 📄 License

Ce projet est sous licence MIT.

---
