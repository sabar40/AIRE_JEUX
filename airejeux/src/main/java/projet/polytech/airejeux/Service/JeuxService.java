package projet.polytech.airejeux.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projet.polytech.airejeux.Entity.Coordonnees;
import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.Repository.JeuxRepository;
import projet.polytech.airejeux.Repository.ReservationRepository;
import projet.polytech.airejeux.dto.JeuxRequestDto;
import projet.polytech.airejeux.exception.ConflictException;
import projet.polytech.airejeux.exception.ResourceNotFoundException;
import projet.polytech.airejeux.mapper.JeuxMapper;

import java.util.List;

@Service
public class JeuxService {

    @Autowired
    private JeuxRepository jeuxRepository;

    @Autowired
    private ReservationRepository reservationRepository; // Indispensable pour le DELETE

    // READ (Tous les jeux)
    public List<Jeux> getAllJeux() {
        return jeuxRepository.findAll();
    }

    // READ (Un jeu par ID)
    public Jeux getJeuById(Long id) {
        return jeuxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeux", "id", id));
    }

    // CREATE
    @Transactional
    public Jeux createJeu(JeuxRequestDto dto) {
        // Le mapper prépare l'objet
        Jeux jeu = JeuxMapper.toEntity(dto);
        // Le service sauvegarde (CascadeType.ALL sauvegardera aussi les coordonnées)
        return jeuxRepository.save(jeu);
    }

    // UPDATE
    @Transactional
    public Jeux updateJeu(Long id, JeuxRequestDto dto) {
        // 1. Trouver le jeu existant
        Jeux jeuExistant = getJeuById(id);

        // 2. Mettre à jour les champs simples
        jeuExistant.setNom(dto.getNom());
        jeuExistant.setQuantite(dto.getQuantite());
        jeuExistant.setDescription(dto.getDescription());

        // 3. Mettre à jour les coordonnées
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
        // 1. VÉRIFICATION DE SÉCURITÉ : Y a-t-il des réservations pour ce jeu ?
        // (On utilise la méthode qu'on avait ajoutée au ReservationRepository)
        if (reservationRepository.existsByJeuxId(id)) {
            // Si oui, on bloque la suppression.
            throw new ConflictException(
                    "Impossible de supprimer le jeu: Des réservations actives existent pour ce jeu.");
        }

        // 2. Trouver le jeu (si on est ici, aucune réservation n'existe)
        Jeux jeu = getJeuById(id);

        // 3. Supprimer le jeu.
        // (Grâce à CascadeType.ALL et orphanRemoval=true sur l'entité Jeux,
        // les coordonnées associées seront aussi supprimées)
        jeuxRepository.delete(jeu);
    }
}