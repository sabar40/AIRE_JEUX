# 🎮 AireJeux - Backend API

API REST Spring Boot pour la gestion et la réservation d'aires de jeux à Tours.

## 📋 Description

Backend de la plateforme AireJeux fournissant une API RESTful sécurisée avec JWT pour la gestion des jeux, réservations et utilisateurs. L'API gère l'authentification, les autorisations par rôle, et la logique métier complète.

## ✨ Fonctionnalités

### 🔐 Authentification & Sécurité
- **JWT Authentication** avec tokens Bearer
- Extraction automatique du rôle depuis le JWT (ROLE_USER, ROLE_ADMIN)
- Guards par endpoint avec `@PreAuthorize`
- BCrypt pour le hachage des mots de passe
- Configuration CORS pour le frontend

### 🎯 Gestion des Jeux
- CRUD complet des jeux (Create, Read, Update, Delete)
- Stockage des coordonnées GPS (latitude, longitude)
- Validation des données avec DTOs
- Repository JPA/Hibernate

### 📅 Gestion des Réservations
- Création de réservations avec date, heure et quantité
- Statuts : PENDING, APPROVED, REJECTED, CANCELLED
- Validation métier :
  - Vérification existence du jeu
  - Contrôle du propriétaire pour annulation
  - Validation des transitions de statut
- Endpoints admin pour validation
- Enrichissement automatique avec le nom du jeu

### 👥 Gestion des Utilisateurs
- Inscription avec rôles (USER/ADMIN)
- Compte admin pré-créé : `admin1` / `admin123`
- Profils utilisateurs complets

## 🛠️ Technologies

- **Framework** : Spring Boot 3.5.6
- **Java** : 22
- **Base de données** : MariaDB 10.6.22
- **ORM** : JPA/Hibernate
- **Sécurité** : Spring Security + JWT (HS512)
- **Build** : Maven
- **Validation** : Hibernate Validator

## 📦 Installation

### Prérequis

- Java 22+
- Maven 3.8+
- MariaDB 10.6+

### Configuration

1. **Cloner le repository**
```bash
git clone https://github.com/sabar40/AIRE_JEUX.git
cd airejeux
```

2. **Configurer la base de données**

Créer l'utilisateur MySQL (si besoin) :
```sql
CREATE USER 'airejeux'@'localhost' IDENTIFIED BY 'airejeux123';
GRANT ALL PRIVILEGES ON airejeux_db.* TO 'airejeux'@'localhost';
FLUSH PRIVILEGES;
```

Importer le schéma (le script crée la base, les tables et les données) :
```bash
mysql -u airejeux -pairejeux123 < src/main/resources/airejeux_structure_complete.sql
```

> **ℹ️ Astuce Windows**
>
> - Pour ouvrir un terminal MySQL sous Windows, utilisez l'invite de commandes (cmd) ou PowerShell :
>   ```cmd
>   mysql -u airejeux -p airejeux_db < src\main\resources\airejeux_structure_complete.sql
>   ```
>   (Remplacez les `/` par `\` dans les chemins sous Windows)
>
> - Pour copier le fichier de configuration exemple :
>   ```cmd
>   copy src\main\resources\application.properties.example src\main\resources\application.properties
>   ```
> - Si vous utilisez XAMPP/WAMP, vérifiez le port (souvent 3306 ou 3307) et adaptez `application.properties`.
> - Si vous avez une erreur d'accès, vérifiez que l'utilisateur MySQL a bien les droits et que le service MariaDB est démarré.

3. **Configuration** (`src/main/resources/application.properties`)
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/airejeux_db?serverTimezone=Europe/Paris
spring.datasource.username=airejeux
spring.datasource.password=airejeux123
spring.jpa.properties.hibernate.jdbc.time_zone=Europe/Paris
```

4. **Démarrer l'application**
```bash
./mvnw spring-boot:run
```

L'API sera accessible sur `http://localhost:8080`

## 🏗️ Structure du Projet

```
src/main/java/projet/polytech/airejeux/
├── AirejeuxApplication.java       # Point d'entrée
├── config/
│   └── SecurityConfig.java        # Configuration Spring Security
├── controller/                     # Contrôleurs REST
│   ├── AuthController.java        # Login/Register
│   ├── JeuxController.java        # CRUD Jeux
│   ├── ReservationController.java # Gestion réservations
│   └── UserController.java        # Gestion utilisateurs
├── dto/                           # Data Transfer Objects
│   ├── JeuxRequestDto.java
│   ├── JeuxResponseDto.java
│   ├── ReservationRequestDto.java
│   ├── ReservationResponseDto.java
│   └── UserDTO.java
├── Entity/                        # Entités JPA
│   ├── Coordonnees.java
│   ├── Jeux.java
│   ├── Reservation.java
│   └── Utilisateur.java
├── exception/                     # Gestion des exceptions
│   ├── GlobalExceptionHandler.java  # @RestControllerAdvice
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── ...
├── mapper/                        # Conversions Entity ↔ DTO
│   ├── JeuxMapper.java
│   ├── ReservationMapper.java
│   └── UtilisateurMapper.java
├── Repository/                    # Repositories JPA
│   ├── JeuxRepository.java
│   ├── ReservationRepository.java
│   └── UtilisateurRepository.java
├── security/                      # Sécurité JWT
│   └── JwtFilter.java            # Extraction JWT + Authorities
├── Service/                       # Logique métier
│   ├── JeuxService.java
│   ├── JwtService.java
│   ├── ReservationService.java
│   └── UtilisateurService.java
└── utils/
    └── ReservationStatus.java    # Constantes de statut
```

## 🔌 Endpoints API

### 🔓 Public

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/login` | Connexion (retourne JWT) |
| POST | `/api/auth/register` | Inscription |
| GET | `/api/jeux` | Liste tous les jeux |

### 🔐 Authentifié (USER)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/reservations` | Créer une réservation |
| GET | `/api/reservations/my-reservations` | Mes réservations |
| PATCH | `/api/reservations/{id}/cancel` | Annuler ma réservation |

### 👨‍💼 Admin uniquement

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/jeux` | Créer un jeu |
| PUT | `/api/jeux/{id}` | Modifier un jeu |
| DELETE | `/api/jeux/{id}` | Supprimer un jeu |
| GET | `/api/reservations/pending` | Réservations en attente |
| PATCH | `/api/reservations/{id}/status` | Valider/Rejeter réservation |

## 🔒 Authentification

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin1",
  "password": "admin123"
}
```

## 📊 Schéma Base de Données

### Tables Principales

- **utilisateur** : Gestion des comptes (username, password_hash, role)
- **jeux** : Catalogue des équipements (nom, description, quantité, coordonnées)
- **coordonnees** : Localisation GPS (latitude, longitude)
- **reservation** : Réservations avec dates, heures, statut

## ⚙️ Configuration Avancée

### Timezone
Les dates/heures utilisent le fuseau Europe/Paris configuré dans :
- `spring.datasource.url` : `?serverTimezone=Europe/Paris`
- `spring.jpa.properties.hibernate.jdbc.time_zone=Europe/Paris`

### JWT Secret
La clé JWT est générée automatiquement (HS512, 512 bits) dans `JwtService.java`.

### Gestion des Erreurs
`GlobalExceptionHandler` avec `@RestControllerAdvice` retourne des erreurs structurées :
- 400 : Bad Request
- 401 : Unauthorized
- 403 : Forbidden
- 404 : Resource Not Found

## 👥 Contributors

- **Christ Chadrak MVOUNGOU** - ccmvoungou@gmail.com
- **Mariem Ejiewen** - [@Mounaejiwene](https://github.com/Mounaejiwene)
- **Sidi Med SABAR** - [@sabar40](https://github.com/sabar40)

## 📄 Licence

Ce projet est développé dans le cadre d'un projet académique à Polytech Tours.

---

**Version** : 1.0.0  
**Spring Boot** : 3.5.6  
**Java** : 22  
**API Documentation** : Consultez les controllers pour les spécifications détaillées
