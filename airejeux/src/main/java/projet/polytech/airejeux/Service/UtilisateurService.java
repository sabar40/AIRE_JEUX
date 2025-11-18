package projet.polytech.airejeux.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Repository.UtilisateurRepository;
import projet.polytech.airejeux.exception.DuplicateResourceException;
import projet.polytech.airejeux.exception.ResourceNotFoundException;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Créer un utilisateur
    public Utilisateur createUser(Utilisateur user) {
        // Vérifier si le username existe déjà
        if (utilisateurRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Utilisateur", "username", user.getUsername());
        }
        return utilisateurRepository.save(user);
    }

    // Récupérer un utilisateur par son ID
    public Utilisateur getUserById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    // Mettre à jour un utilisateur
    public Utilisateur updateUser(Long id, Utilisateur user) {
        // Vérifier que l'utilisateur existe
        Utilisateur existingUser = getUserById(id);

        // Vérifier si le nouveau username est déjà pris par un autre utilisateur
        if (!user.getUsername().equals(existingUser.getUsername()) &&
                utilisateurRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Utilisateur", "username", user.getUsername());
        }

        user.setId(id); // Assurer que l'ID ne change pas
        return utilisateurRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        // Vérifier que l'utilisateur existe avant de le supprimer
        getUserById(id);
        utilisateurRepository.deleteById(id);
    }
}
