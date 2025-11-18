package projet.polytech.airejeux.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import projet.polytech.airejeux.dto.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le GlobalExceptionHandler
 * Ces tests vérifient que chaque exception est bien convertie en ErrorResponse
 * avec le bon code HTTP
 */
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler exceptionHandler;
  private HttpServletRequest request;

  @BeforeEach
  void setUp() {
    exceptionHandler = new GlobalExceptionHandler();
    MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    mockRequest.setRequestURI("/api/test");
    request = mockRequest;
  }

  @Test
  void testResourceNotFoundException() {
    // Given
    ResourceNotFoundException exception = new ResourceNotFoundException("Jeux", "id", 999L);

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, request);

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(404, response.getBody().getStatus());
    assertEquals("Not Found", response.getBody().getError());
    assertTrue(response.getBody().getMessage().contains("Jeux"));
    assertTrue(response.getBody().getMessage().contains("id"));
    assertTrue(response.getBody().getMessage().contains("999"));
    assertEquals("/api/test", response.getBody().getPath());
  }

  @Test
  void testDuplicateResourceException() {
    // Given
    DuplicateResourceException exception = new DuplicateResourceException("Utilisateur", "username", "john");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateResourceException(exception, request);

    // Then
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(409, response.getBody().getStatus());
    assertEquals("Conflict", response.getBody().getError());
    assertTrue(response.getBody().getMessage().contains("Utilisateur"));
    assertTrue(response.getBody().getMessage().contains("username"));
    assertTrue(response.getBody().getMessage().contains("john"));
  }

  @Test
  void testUnauthorizedException() {
    // Given
    UnauthorizedException exception = new UnauthorizedException("Nom d'utilisateur ou mot de passe incorrect");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnauthorizedException(exception, request);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(401, response.getBody().getStatus());
    assertEquals("Unauthorized", response.getBody().getError());
    assertEquals("Nom d'utilisateur ou mot de passe incorrect", response.getBody().getMessage());
  }

  @Test
  void testForbiddenException() {
    // Given
    ForbiddenException exception = new ForbiddenException("Vous n'avez pas les droits pour cette opération");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleForbiddenException(exception, request);

    // Then
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(403, response.getBody().getStatus());
    assertEquals("Forbidden", response.getBody().getError());
    assertEquals("Vous n'avez pas les droits pour cette opération", response.getBody().getMessage());
  }

  @Test
  void testBadRequestException() {
    // Given
    BadRequestException exception = new BadRequestException("Données invalides");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadRequestException(exception, request);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(400, response.getBody().getStatus());
    assertEquals("Bad Request", response.getBody().getError());
    assertEquals("Données invalides", response.getBody().getMessage());
  }

  @Test
  void testConflictException() {
    // Given
    ConflictException exception = new ConflictException(
        "Impossible de supprimer ce jeu car des réservations sont actives");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleConflictException(exception, request);

    // Then
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(409, response.getBody().getStatus());
    assertEquals("Conflict", response.getBody().getError());
    assertTrue(response.getBody().getMessage().contains("Impossible de supprimer"));
  }

  @Test
  void testReservationException() {
    // Given
    ReservationException exception = new ReservationException("Date de réservation invalide");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleReservationException(exception, request);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(400, response.getBody().getStatus());
    assertEquals("Bad Request", response.getBody().getError());
    assertEquals("Date de réservation invalide", response.getBody().getMessage());
  }

  @Test
  void testInvalidTokenException() {
    // Given
    InvalidTokenException exception = new InvalidTokenException("Token JWT invalide");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidTokenException(exception, request);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(401, response.getBody().getStatus());
    assertEquals("Unauthorized", response.getBody().getError());
    assertEquals("Token JWT invalide", response.getBody().getMessage());
  }

  @Test
  void testGenericException() {
    // Given
    Exception exception = new RuntimeException("Erreur inattendue");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(exception, request);

    // Then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(500, response.getBody().getStatus());
    assertEquals("Internal Server Error", response.getBody().getError());
    assertEquals("Une erreur interne s'est produite. Veuillez contacter l'administrateur.",
        response.getBody().getMessage());
  }

  @Test
  void testErrorResponseContainsTimestamp() {
    // Given
    BadRequestException exception = new BadRequestException("Test");

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadRequestException(exception, request);

    // Then
    assertNotNull(response.getBody().getTimestamp());
  }

  @Test
  void testErrorResponseContainsPath() {
    // Given
    MockHttpServletRequest customRequest = new MockHttpServletRequest();
    customRequest.setRequestURI("/api/jeux/123");
    ResourceNotFoundException exception = new ResourceNotFoundException("Jeux", "id", 123L);

    // When
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, customRequest);

    // Then
    assertEquals("/api/jeux/123", response.getBody().getPath());
  }
}
