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

Créer la base de données :
```sql
CREATE DATABASE airejeux_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'airejeux'@'localhost' IDENTIFIED BY 'airejeux123';
GRANT ALL PRIVILEGES ON airejeux_db.* TO 'airejeux'@'localhost';
FLUSH PRIVILEGES;
```

Importer le schéma :
```bash
mysql -u airejeux -pairejeux123 airejeux_db < src/main/resources/airejeux_structure_complete.sql
```

3. **Configuration** (`src/main/resources/application.properties`)

Vérifier/modifier la configuration selon votre environnement :
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/airejeux_db?serverTimezone=Europe/Paris
spring.datasource.username=airejeux
spring.datasource.password=airejeux123
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.jpa.properties.hibernate.jdbc.time_zone=Europe/Paris
```

4. **Compiler et démarrer l'application**

Option 1 : Avec Maven Wrapper (recommandé)
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Option 2 : Avec Maven global
```bash
mvn clean install
mvn spring-boot:run
```

Option 3 : Via JAR compilé
```bash
./mvnw clean package
java -jar target/airejeux-0.0.1-SNAPSHOT.jar
```

L'API sera accessible sur `http://localhost:8080`

**Vérifier le démarrage :**
```bash
curl http://localhost:8080/api/jeux
```

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
