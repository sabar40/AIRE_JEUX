package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsque la requête contient des données invalides.
 * Exemple : Champs manquants, format incorrect, valeurs hors limites
 */
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
