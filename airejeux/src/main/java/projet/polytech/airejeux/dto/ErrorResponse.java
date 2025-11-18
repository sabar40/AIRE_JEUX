package projet.polytech.airejeux.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO standardisé pour les réponses d'erreur de l'API.
 * Fournit des informations détaillées sur les erreurs survenues.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  /**
   * Timestamp de l'erreur
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime timestamp;

  /**
   * Code de statut HTTP (400, 404, 500, etc.)
   */
  private int status;

  /**
   * Nom du statut HTTP (Bad Request, Not Found, etc.)
   */
  private String error;

  /**
   * Message d'erreur principal
   */
  private String message;

  /**
   * Chemin de la requête qui a généré l'erreur
   */
  private String path;

  /**
   * Liste des erreurs de validation (optionnel)
   */
  private List<ValidationError> validationErrors;

  /**
   * Constructeur simplifié sans erreurs de validation
   */
  public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.validationErrors = new ArrayList<>();
  }

  /**
   * Ajoute une erreur de validation à la liste
   */
  public void addValidationError(String field, String message) {
    if (validationErrors == null) {
      validationErrors = new ArrayList<>();
    }
    validationErrors.add(new ValidationError(field, message));
  }

  /**
   * Classe interne pour représenter une erreur de validation
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ValidationError {
    private String field;
    private String message;
  }
}
