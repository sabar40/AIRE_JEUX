package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsqu'un token JWT est invalide, expiré ou malformé.
 */
public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
