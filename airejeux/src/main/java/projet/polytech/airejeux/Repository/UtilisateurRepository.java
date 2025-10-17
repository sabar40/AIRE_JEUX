package projet.polytech.airejeux.Repository;

import projet.polytech.airejeux.Entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Tu peux ajouter des méthodes personnalisées si nécessaire, comme la recherche par mail ou username.
}
