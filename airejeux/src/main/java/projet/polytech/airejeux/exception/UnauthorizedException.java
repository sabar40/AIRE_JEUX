package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsqu'un utilisateur tente d'accéder à une ressource
 * sans être authentifié ou avec des credentials invalides.
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }
}
