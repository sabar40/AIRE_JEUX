package projet.polytech.airejeux.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projet.polytech.airejeux.Entity.Coordonnees;
import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.Repository.JeuxRepository;
import projet.polytech.airejeux.Repository.ReservationRepository;
import projet.polytech.airejeux.dto.JeuxRequestDto;
import projet.polytech.airejeux.mapper.JeuxMapper;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class JeuxService {

    @Autowired
    private JeuxRepository jeuxRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private JeuxMapper jeuxMapper; // Injection du Mapper

    // --- CES MÉTHODES MANQUAIENT PROBABLEMENT ---
    
    // READ (Tous les jeux)
    public List<Jeux> getAllJeux() {
        return jeuxRepository.findAll();
    }

    // READ (Un jeu par ID)
    public Jeux getJeuById(Long id) {
        return jeuxRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jeu non trouvé avec l'id : " + id));
    }
    // --- FIN DES MÉTHODES MANQUANTES ---

    // CREATE
    @Transactional
    public Jeux createJeu(JeuxRequestDto dto) {
        Jeux jeu = jeuxMapper.toEntity(dto);
        return jeuxRepository.save(jeu);
    }

    // UPDATE
    @Transactional
    public Jeux updateJeu(Long id, JeuxRequestDto dto) {
        // Appelle la méthode getJeuById de CETTE classe
        Jeux jeuExistant = this.getJeuById(id); 
        
        jeuExistant.setNom(dto.getNom());
        jeuExistant.setQuantite(dto.getQuantite());
        jeuExistant.setDescription(dto.getDescription());
        
        Coordonnees coordonneesExistantes = jeuExistant.getCoordonnees();
        if (coordonneesExistantes == null) {
            coordonneesExistantes = new Coordonnees();
        }
        coordonneesExistantes.setLatitude(dto.getCoordonnees().getLatitude());
        coordonneesExistantes.setLongitude(dto.getCoordonnees().getLongitude());
        jeuExistant.setCoordonnees(coordonneesExistantes);

        return jeuxRepository.save(jeuExistant);
    }

    // DELETE
    @Transactional
    public void deleteJeu(Long id) {
        if (reservationRepository.existsByJeuxId(id)) {
            throw new IllegalStateException("Impossible de supprimer le jeu: Des réservations existent pour ce jeu.");
        }
        // Appelle la méthode getJeuById de CETTE classe
        Jeux jeu = this.getJeuById(id); 
        jeuxRepository.delete(jeu);
    }
}