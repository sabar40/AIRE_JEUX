package projet.polytech.airejeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Nécessite @EnableMethodSecurity
import org.springframework.web.bind.annotation.*;
import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Repository.JeuxRepository;
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

    @Autowired
    private JeuxRepository jeuxRepository;

    // --- Endpoints pour UTILISATEURS connectés (USER et ADMIN) ---

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody ReservationRequestDto dto,
            Principal principal) {

        Reservation nouvelleReservation = reservationService.createReservation(dto, principal.getName());
        Jeux jeux = jeuxRepository.findById(nouvelleReservation.getJeuxId()).orElse(null);
        ReservationResponseDto responseDto = ReservationMapper.toDto(nouvelleReservation, jeux);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/my-reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(Principal principal) {
        List<Reservation> reservations = reservationService.getMyReservations(principal.getName());
        List<ReservationResponseDto> dtos = reservations.stream()
                .map(r -> {
                    Jeux jeux = jeuxRepository.findById(r.getJeuxId()).orElse(null);
                    return ReservationMapper.toDto(r, jeux);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDto> cancelMyReservation(@PathVariable Long id, Principal principal) {
        Reservation reservation = reservationService.cancelMyReservation(id, principal.getName());
        Jeux jeux = jeuxRepository.findById(reservation.getJeuxId()).orElse(null);
        return ResponseEntity.ok(ReservationMapper.toDto(reservation, jeux));
    }

    // --- Endpoints pour ADMINS ---

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponseDto>> getPendingReservations() {
        List<Reservation> reservations = reservationService.getReservationsByStatus(ReservationStatus.PENDING);
        List<ReservationResponseDto> dtos = reservations.stream()
                .map(r -> {
                    Jeux jeux = jeuxRepository.findById(r.getJeuxId()).orElse(null);
                    return ReservationMapper.toDto(r, jeux);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationResponseDto> updateReservationStatus(
            @PathVariable Long id,
            @RequestBody ReservationUpdateStatusDto dto) {

        Reservation reservation = reservationService.updateReservationStatus(id, dto);
        Jeux jeux = jeuxRepository.findById(reservation.getJeuxId()).orElse(null);
        return ResponseEntity.ok(ReservationMapper.toDto(reservation, jeux));
    }
}