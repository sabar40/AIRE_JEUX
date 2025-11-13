package projet.polytech.airejeux.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.polytech.airejeux.Entity.Jeux;

@Repository
public interface JeuxRepository extends JpaRepository<Jeux, Long> {
}