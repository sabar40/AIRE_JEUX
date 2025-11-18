package projet.polytech.airejeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Nécessite @EnableMethodSecurity
import org.springframework.web.bind.annotation.*;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Service.ReservationService;
import projet.polytech.airejeux.dto.ReservationRequestDto;
import projet.polytech.airejeux.dto.ReservationResponseDto; // DTO de réponse
import projet.polytech.airejeux.dto.ReservationUpdateStatusDto;
import projet.polytech.airejeux.mapper.ReservationMapper; // Mapper
import projet.polytech.airejeux.utils.ReservationStatus;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors; // Pour mapper les listes

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // --- Endpoints pour UTILISATEURS connectés (USER et ADMIN) ---

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto dto,
            Principal principal) {

        Reservation nouvelleReservation = reservationService.createReservation(dto, principal.getName());
        // On convertit l'entité en DTO de réponse
        ReservationResponseDto responseDto = ReservationMapper.toDto(nouvelleReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/my-reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(Principal principal) {
        List<Reservation> reservations = reservationService.getMyReservations(principal.getName());
        // On convertit la liste d'entités en liste de DTOs
        List<ReservationResponseDto> dtos = reservations.stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDto> cancelMyReservation(@PathVariable Long id, Principal principal) {
        Reservation reservation = reservationService.cancelMyReservation(id, principal.getName());
        return ResponseEntity.ok(ReservationMapper.toDto(reservation));
    }

    // --- Endpoints pour ADMINS ---

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponseDto>> getPendingReservations() {
        List<Reservation> reservations = reservationService.getReservationsByStatus(ReservationStatus.PENDING);
        List<ReservationResponseDto> dtos = reservations.stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationResponseDto> updateReservationStatus(
            @PathVariable Long id,
            @RequestBody ReservationUpdateStatusDto dto) {

        Reservation reservation = reservationService.updateReservationStatus(id, dto);
        return ResponseEntity.ok(ReservationMapper.toDto(reservation));
    }
}