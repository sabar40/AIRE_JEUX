package projet.polytech.airejeux.exception;

/**
 * Exception levée pour les erreurs liées aux réservations.
 * Exemples :
 * - Conflit de créneaux horaires
 * - Quantité insuffisante
 * - Tentative d'annulation d'une réservation déjà annulée
 */
public class ReservationException extends RuntimeException {

  public ReservationException(String message) {
    super(message);
  }

  public ReservationException(String message, Throwable cause) {
    super(message, cause);
  }
}
