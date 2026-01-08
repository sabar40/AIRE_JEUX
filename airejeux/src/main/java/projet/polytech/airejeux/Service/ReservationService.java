package projet.polytech.airejeux.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Entity.Jeux; // Assurez-vous de l'import
import projet.polytech.airejeux.Repository.JeuxRepository;
import projet.polytech.airejeux.Repository.ReservationRepository;
import projet.polytech.airejeux.Repository.UtilisateurRepository;
import projet.polytech.airejeux.dto.ReservationRequestDto;
import projet.polytech.airejeux.dto.ReservationUpdateStatusDto;
import projet.polytech.airejeux.exception.BadRequestException;
import projet.polytech.airejeux.exception.ForbiddenException;
import projet.polytech.airejeux.exception.ReservationException;
import projet.polytech.airejeux.exception.ResourceNotFoundException;
import projet.polytech.airejeux.mapper.ReservationMapper;
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

        // 1. Récupérer le jeu
        Jeux jeu = jeuxRepository.findById(dto.getJeuxId())
                .orElseThrow(() -> new ResourceNotFoundException("Jeux", "id", dto.getJeuxId()));

        // 2. Vérifier si le stock est suffisant
        if (jeu.getQuantite() < dto.getQuantity()) {
            throw new BadRequestException("Stock insuffisant pour ce jeu. Disponible : " + jeu.getQuantite());
        }

        // 3. Déduire la quantité du stock du jeu
        jeu.setQuantite(jeu.getQuantite() - dto.getQuantity());
        jeuxRepository.save(jeu);

        // 4. Mapper et sauvegarder la réservation
        Reservation reservation = ReservationMapper.toEntity(dto, utilisateur);
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

            // RESTITUTION DU STOCK : On récupère le jeu et on lui rend la quantité
            Jeux jeu = jeuxRepository.findById(reservation.getJeuxId())
                    .orElseThrow(() -> new ResourceNotFoundException("Jeux", "id", reservation.getJeuxId()));
            
            jeu.setQuantite(jeu.getQuantite() + reservation.getQuantity());
            jeuxRepository.save(jeu);

            reservation.setStatus(ReservationStatus.CANCELLED);
            return reservationRepository.save(reservation);
        } else {
            throw new ReservationException("Impossible d'annuler une réservation déjà " + reservation.getStatus());
        }
    }

    public List<Reservation> getReservationsByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    @Transactional
    public Reservation updateReservationStatus(Long reservationId, ReservationUpdateStatusDto dto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        if (!reservation.getStatus().equals(ReservationStatus.PENDING)) {
            throw new ReservationException("Cette réservation n'est plus en attente.");
        }

        String newStatus = dto.getStatus().toUpperCase();
        
        // Si l'admin REJETTE une réservation PENDING, on rend aussi le stock
        if (newStatus.equals(ReservationStatus.REJECTED)) {
            Jeux jeu = jeuxRepository.findById(reservation.getJeuxId())
                    .orElseThrow(() -> new ResourceNotFoundException("Jeux", "id", reservation.getJeuxId()));
            jeu.setQuantite(jeu.getQuantite() + reservation.getQuantity());
            jeuxRepository.save(jeu);
        }

        if (newStatus.equals(ReservationStatus.APPROVED) || newStatus.equals(ReservationStatus.REJECTED)) {
            reservation.setStatus(newStatus);
            return reservationRepository.save(reservation);
        } else {
            throw new BadRequestException("Statut invalide.");
        }
    }
}