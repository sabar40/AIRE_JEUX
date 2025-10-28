package projet.polytech.airejeux.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Créer un utilisateur
    public Utilisateur createUser(Utilisateur user) {
        return utilisateurRepository.save(user);
    }

    // Récupérer un utilisateur par son ID
    public Optional<Utilisateur> getUserById(Long id) {
        return utilisateurRepository.findById(id);
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    // Mettre à jour un utilisateur
    public Utilisateur updateUser(Long id, Utilisateur user) {
        user.setId(id); // Assurer que l'ID ne change pas
        return utilisateurRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        utilisateurRepository.deleteById(id);
    }
}
