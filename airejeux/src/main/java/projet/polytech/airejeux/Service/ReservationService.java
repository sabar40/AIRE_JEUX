package projet.polytech.airejeux.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Repository.JeuxRepository;
import projet.polytech.airejeux.Repository.ReservationRepository;
import projet.polytech.airejeux.Repository.UtilisateurRepository;
import projet.polytech.airejeux.dto.ReservationRequestDto;
import projet.polytech.airejeux.dto.ReservationUpdateStatusDto;
import projet.polytech.airejeux.mapper.ReservationMapper;
import projet.polytech.airejeux.utils.ReservationStatus;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private JeuxRepository jeuxRepository;
    
    @Autowired
    private ReservationMapper reservationMapper; // <-- INJECTION DU MAPPER

    // CREATE
    @Transactional
    public Reservation createReservation(ReservationRequestDto dto, String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        if (!jeuxRepository.existsById(dto.getJeuxId())) {
            throw new EntityNotFoundException("Jeu non trouvé avec l'id : " + dto.getJeuxId());
        }

        // 1. MapStruct convertit le DTO
        Reservation reservation = reservationMapper.toEntity(dto);
        
        // 2. On définit manuellement les champs que MapStruct a ignorés
        reservation.setUtilisateur(utilisateur);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservation(0); // Pour le bug "cannot be null"

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getMyReservations(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        return reservationRepository.findByUtilisateurId(utilisateur.getId());
    }

    @Transactional
    public Reservation cancelMyReservation(Long reservationId, String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée"));

        if (!reservation.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new SecurityException("Accès non autorisé à cette réservation");
        }

        if (reservation.getStatus().equals(ReservationStatus.APPROVED) || 
            reservation.getStatus().equals(ReservationStatus.PENDING)) {
            
            reservation.setStatus(ReservationStatus.CANCELLED);
            return reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("Impossible d'annuler une réservation déjà " + reservation.getStatus());
        }
    }

    // --- LOGIQUE ADMIN ---

    public List<Reservation> getReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationUpdateStatusDto dto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée"));

        if (!reservation.getStatus().equals(ReservationStatus.PENDING)) {
            throw new IllegalStateException("Cette réservation n'est plus en attente.");
        }

        String newStatus = dto.getStatus().toUpperCase();
        if (newStatus.equals(ReservationStatus.APPROVED) || newStatus.equals(ReservationStatus.REJECTED)) {
            reservation.setStatus(newStatus);
            return reservationRepository.save(reservation);
        } else {
            throw new IllegalArgumentException("Statut invalide : " + newStatus);
        }
    }
}