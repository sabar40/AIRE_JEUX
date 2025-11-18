package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsqu'une opération entre en conflit avec l'état actuel des
 * données.
 * Exemples :
 * - Tentative de suppression d'un jeu ayant des réservations actives
 * - Réservation avec conflit horaire
 * - Quantité insuffisante pour une réservation
 */
public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }

  public ConflictException(String message, Throwable cause) {
    super(message, cause);
  }
}
