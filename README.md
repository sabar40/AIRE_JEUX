# AIRE_JEUX

#1. API : Authentification (Login / Register)
1.1 Login - POST /api/auth/login

Description : Cette API permet à un utilisateur de se connecter à l'application en envoyant son nom d'utilisateur et son mot de passe. Si l'utilisateur est authentifié avec succès, un JWT (JSON Web Token) est généré et renvoyé pour l'utilisateur.

Méthode HTTP : POST

Endpoint : /api/auth/login

Request Body :

{
  "username": "utilisateur1",
  "password": "motdepasse"
}

Réponse :

200 OK si l'authentification est réussie. Le token JWT est retourné.

401 Unauthorized si l'authentification échoue.

Exemple de réponse :
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...:token


1.2 Register - POST /api/auth/register

Description : Cette API permet d'enregistrer un nouvel utilisateur. Les informations comme le nom, le prénom, l'email, le mot de passe, etc., sont envoyées pour créer un nouvel utilisateur.

Méthode HTTP : POST

Endpoint : /api/auth/register

Request Body :

{
  "username": "newUser",
  "password": "newPassword123",
  "nom": "...",
  "prenom": "...",
  "mail": "....@example.com",
  "role": "USER"
}
Réponse :

201 Created si l'utilisateur est créé avec succès.

400 Bad Request si les données sont invalides

#2. API : CRUD Utilisateur
2.1 Créer un utilisateur - POST /api/users

Description : Cette API permet de créer un nouvel utilisateur dans la base de données en utilisant un UserDTO.

Méthode HTTP : POST

Endpoint : /api/users

Request Body :
{
  "username": "johnDoe",
  "password": "password123",
  "nom": "Doe",
  "prenom": "John",
  "mail": "john.doe@example.com",
  "role": "USER"
}
{
  "username": "...",
  "password": "password123",
  "nom": "...",
  "prenom": ",,,,",
  "mail": "....@example.com",
  "role": "USER"
}


    Réponse :

        201 Created si l'utilisateur est créé avec succès.

        400 Bad Request si les données envoyées sont incorrectes.

2.2 Récupérer un utilisateur par ID - GET /api/users/{id}

Description : Cette API permet de récupérer les informations d'un utilisateur spécifique en utilisant son ID.

Méthode HTTP : GET

Endpoint : /api/users/{id}

Réponse :

200 OK si l'utilisateur est trouvé.

404 Not Found si l'utilisateur avec l'ID donné n'existe pas.


2.3 Récupérer tous les utilisateurs - GET /api/users

Description : Cette API permet de récupérer tous les utilisateurs de l'application.

Méthode HTTP : GET

Endpoint : /api/users

Réponse :

200 OK avec la liste des utilisateurs.


2.4 Mettre à jour un utilisateur - PUT /api/users/{id}

Description : Cette API permet de mettre à jour les informations d'un utilisateur existant en utilisant son ID.

Méthode HTTP : PUT

Endpoint : /api/users/{id}



2.5 Supprimer un utilisateur
Route : DELETE /api/users/{id}
Description :

Cette route permet de supprimer un utilisateur spécifique en utilisant son ID.

Requête :

Méthode HTTP : DELETE

URL : /api/users/{id}

Paramètre :

{id} : L'ID de l'utilisateur que tu souhaites supprimer.

Réponse :

Code de statut HTTP : 204 No Content (si l'utilisateur est supprimé avec succès) ou 404 Not Found (si l'utilisateur n'existe pas)

Corps de la réponse : Aucun contenu, car la ressource a été supprimée.