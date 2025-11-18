package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsqu'une ressource demandée n'existe pas dans la base de
 * données.
 * Exemple : Utilisateur, Jeu, Réservation introuvable
 */
public class ResourceNotFoundException extends RuntimeException {

  private final String resourceName;
  private final String fieldName;
  private final Object fieldValue;

  /**
   * Constructeur avec message
   */
  public ResourceNotFoundException(String message) {
    super(message);
    this.resourceName = null;
    this.fieldName = null;
    this.fieldValue = null;
  }

  /**
   * Constructeur avec détails de la ressource
   * Exemple : ResourceNotFoundException("Utilisateur", "id", 123)
   */
  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s non trouvé(e) avec %s : '%s'", resourceName, fieldName, fieldValue));
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Object getFieldValue() {
    return fieldValue;
  }
}
