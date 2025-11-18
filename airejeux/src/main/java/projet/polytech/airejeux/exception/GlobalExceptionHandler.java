package projet.polytech.airejeux.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import projet.polytech.airejeux.dto.ErrorResponse;

import java.time.LocalDateTime;

/**
 * Gestionnaire global des exceptions pour l'API.
 * Intercepte toutes les exceptions levées par les contrôleurs et les transforme
 * en réponses HTTP standardisées avec un ErrorResponse.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Gère les exceptions de ressource non trouvée (404)
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Gère les exceptions EntityNotFoundException de JPA (404)
   * Pour compatibilité avec le code existant
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  /**
   * Gère les exceptions de mauvaise requête (400)
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(
      BadRequestException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  
}
