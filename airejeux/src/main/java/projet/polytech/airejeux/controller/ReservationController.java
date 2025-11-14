package projet.polytech.airejeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Service.ReservationService;
import projet.polytech.airejeux.dto.ReservationRequestDto;
import projet.polytech.airejeux.dto.ReservationResponseDto;
import projet.polytech.airejeux.dto.ReservationUpdateStatusDto;
import projet.polytech.airejeux.mapper.ReservationMapper; 
import projet.polytech.airejeux.utils.ReservationStatus;
import jakarta.persistence.EntityNotFoundException;

import java.security.Principal; 
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*") 
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper; // <-- INJECTION DU BEAN

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReservation(
            @RequestBody ReservationRequestDto dto, 
            Principal principal) {
        
        try {
            Reservation nouvelleReservation = reservationService.createReservation(dto, principal.getName());
            // Utilise l'instance 'reservationMapper' (non-statique)
            ReservationResponseDto responseDto = reservationMapper.toDto(nouvelleReservation); 
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/my-reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(Principal principal) {
        List<Reservation> reservations = reservationService.getMyReservations(principal.getName());
        List<ReservationResponseDto> dtos = reservations.stream()
                .map(reservationMapper::toDto) // <-- Utilise l'instance 'reservationMapper'
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelMyReservation(@PathVariable Long id, Principal principal) {
        try {
            Reservation reservation = reservationService.cancelMyReservation(id, principal.getName());
            return ResponseEntity.ok(reservationMapper.toDto(reservation)); // <-- Utilise l'instance
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponseDto>> getPendingReservations() {
        List<Reservation> reservations = reservationService.getReservationsByStatus(ReservationStatus.PENDING);
        List<ReservationResponseDto> dtos = reservations.stream()
                .map(reservationMapper::toDto) // <-- Utilise l'instance
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable Long id, 
            @RequestBody ReservationUpdateStatusDto dto) {
        
        try {
            Reservation reservation = reservationService.updateReservationStatus(id, dto);
            return ResponseEntity.ok(reservationMapper.toDto(reservation)); // <-- Utilise l'instance
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}