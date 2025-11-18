package projet.polytech.airejeux.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Repository.JeuxRepository;
import projet.polytech.airejeux.Repository.ReservationRepository;
import projet.polytech.airejeux.Repository.UtilisateurRepository;
import projet.polytech.airejeux.dto.ReservationRequestDto; // DTO de requête
import projet.polytech.airejeux.dto.ReservationUpdateStatusDto;
import projet.polytech.airejeux.exception.BadRequestException;
import projet.polytech.airejeux.exception.ForbiddenException;
import projet.polytech.airejeux.exception.ReservationException;
import projet.polytech.airejeux.exception.ResourceNotFoundException;
import projet.polytech.airejeux.mapper.ReservationMapper; // Mapper
import projet.polytech.airejeux.utils.ReservationStatus;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private JeuxRepository jeuxRepository;

    @Transactional
    public Reservation createReservation(ReservationRequestDto dto, String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "username", username));

        if (!jeuxRepository.existsById(dto.getJeuxId())) {
            throw new ResourceNotFoundException("Jeux", "id", dto.getJeuxId());
        }

        // Utilisation du Mapper pour créer l'entité
        Reservation reservation = ReservationMapper.toEntity(dto, utilisateur);

        // Logique métier : le service définit le statut
        reservation.setStatus(ReservationStatus.PENDING);

        reservation.setReservation(0);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getMyReservations(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "username", username));

        return reservationRepository.findByUtilisateurId(utilisateur.getId());
    }

    @Transactional
    public Reservation cancelMyReservation(Long reservationId, String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "username", username));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        if (!reservation.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new ForbiddenException("Vous n'êtes pas autorisé à annuler cette réservation");
        }

        if (reservation.getStatus().equals(ReservationStatus.APPROVED) ||
                reservation.getStatus().equals(ReservationStatus.PENDING)) {

            reservation.setStatus(ReservationStatus.CANCELLED);
            return reservationRepository.save(reservation);
        } else {
            throw new ReservationException("Impossible d'annuler une réservation déjà " + reservation.getStatus());
        }
    }

    // --- LOGIQUE ADMIN ---

    public List<Reservation> getReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationUpdateStatusDto dto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        if (!reservation.getStatus().equals(ReservationStatus.PENDING)) {
            throw new ReservationException(
                    "Cette réservation n'est plus en attente (statut actuel : " + reservation.getStatus() + ")");
        }

        String newStatus = dto.getStatus().toUpperCase();
        if (newStatus.equals(ReservationStatus.APPROVED) || newStatus.equals(ReservationStatus.REJECTED)) {
            reservation.setStatus(newStatus);
            return reservationRepository.save(reservation);
        } else {
            throw new BadRequestException(
                    "Statut invalide : " + newStatus + ". Statuts autorisés : APPROVED, REJECTED");
        }
    }
}