package projet.polytech.airejeux.mapper;

import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.dto.ReservationRequestDto;
import projet.polytech.airejeux.dto.ReservationResponseDto;

public class ReservationMapper {

    /**
     * Convertit un DTO de requête et un Utilisateur en Entité Reservation
     */
    public static Reservation toEntity(ReservationRequestDto dto, Utilisateur utilisateur) {
        Reservation reservation = new Reservation();
        reservation.setUtilisateur(utilisateur);
        reservation.setJeuxId(dto.getJeuxId());
        reservation.setBookingDate(dto.getBookingDate());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setQuantity(dto.getQuantity());
        reservation.setNotes(dto.getNotes());
        // Le statut est géré par le Service (logique métier)
        return reservation;
    }

    /**
     * Convertit une Entité Reservation en DTO de Réponse
     */
    public static ReservationResponseDto toDto(Reservation entity, Jeux jeux) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(entity.getId());
        dto.setJeuxId(entity.getJeuxId());
        dto.setBookingDate(entity.getBookingDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setQuantity(entity.getQuantity());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());

        // Ajouter les infos de l'utilisateur
        if (entity.getUtilisateur() != null) {
            dto.setUtilisateurId(entity.getUtilisateur().getId());
            dto.setUtilisateurUsername(entity.getUtilisateur().getUsername());
        }

        // Ajouter le nom du jeu
        if (jeux != null) {
            dto.setJeuxNom(jeux.getNom());
        }

        return dto;
    }
}