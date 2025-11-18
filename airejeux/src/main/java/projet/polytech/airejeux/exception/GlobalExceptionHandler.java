package projet.polytech.airejeux.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  /**
   * Gère les erreurs de validation des DTOs (400)
   * Levée automatiquement par @Valid sur les @RequestBody
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "Erreur de validation des données",
        request.getRequestURI());

    // Ajouter les erreurs de validation
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      error.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Gère les exceptions d'authentification (401)
   */
  @ExceptionHandler({ UnauthorizedException.class, AuthenticationException.class, BadCredentialsException.class })
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(
      Exception ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        ex.getMessage() != null ? ex.getMessage() : "Authentification requise ou credentials invalides",
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Gère les exceptions d'accès interdit (403)
   */
  @ExceptionHandler({ ForbiddenException.class, AccessDeniedException.class })
  public ResponseEntity<ErrorResponse> handleForbiddenException(
      Exception ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.FORBIDDEN.value(),
        HttpStatus.FORBIDDEN.getReasonPhrase(),
        ex.getMessage() != null ? ex.getMessage() : "Accès interdit : permissions insuffisantes",
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  /**
   * Gère les exceptions de conflit (409)
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(
      ConflictException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  /**
   * Gère les exceptions de ressource dupliquée (409)
   */
  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
      DuplicateResourceException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  /**
   * Gère les exceptions de token invalide (401)
   */
  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponse> handleInvalidTokenException(
      InvalidTokenException ex,
      HttpServletRequest request) {

    ErrorResponse error = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  
}
