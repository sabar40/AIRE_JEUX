package projet.polytech.airejeux.exception;

/**
 * Exception levée lorsqu'on tente de créer une ressource qui existe déjà.
 * Exemple : Username ou email déjà utilisé lors de l'inscription
 */
public class DuplicateResourceException extends RuntimeException {

  private final String resourceName;
  private final String fieldName;
  private final Object fieldValue;

  public DuplicateResourceException(String message) {
    super(message);
    this.resourceName = null;
    this.fieldName = null;
    this.fieldValue = null;
  }

  public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s existe déjà avec %s : '%s'", resourceName, fieldName, fieldValue));
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
