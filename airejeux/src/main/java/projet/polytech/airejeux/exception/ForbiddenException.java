package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsqu'un utilisateur authentifié tente d'accéder à une
 * ressource
 * pour laquelle il n'a pas les permissions nécessaires.
 * Exemple : USER essayant d'accéder à une fonction ADMIN
 */
public class ForbiddenException extends RuntimeException {

  public ForbiddenException(String message) {
    super(message);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause);
  }
}
