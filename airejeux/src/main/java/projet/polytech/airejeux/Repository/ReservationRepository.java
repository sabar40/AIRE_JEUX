package projet.polytech.airejeux.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.polytech.airejeux.Entity.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // (Ajoutez ces méthodes au fichier de votre collègue)

    /**
     * Trouve toutes les réservations pour un utilisateur donné.
     * (Pour l'endpoint "Mes Réservations" de l'utilisateur)
     */
    List<Reservation> findByUtilisateurId(Long utilisateurId);

    /**
     * Trouve toutes les réservations ayant un statut spécifique.
     * (Pour l'admin, ex: voir toutes les "PENDING")
     */
    List<Reservation> findByStatus(String status);
    
    /**
     * Vérifie si une réservation existe pour un jeu spécifique.
     * (Nécessaire pour le CRUD Jeux, pour empêcher la suppression)
     */
    boolean existsByJeuxId(Long jeuxId); 
}