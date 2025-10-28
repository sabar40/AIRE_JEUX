package projet.polytech.airejeux.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Service.UtilisateurService;
import projet.polytech.airejeux.dto.UserDTO;
import projet.polytech.airejeux.mapper.UserMapper;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Création d'un utilisateur
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        // Convertir UserDTO en Utilisateur
        Utilisateur user = UserMapper.toEntity(userDTO);
        Utilisateur savedUser = utilisateurService.createUser(user);
        
        // Convertir Utilisateur en UserDTO pour le retour
        UserDTO savedUserDTO = UserMapper.toDTO(savedUser);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
    }

    // Récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> user = utilisateurService.getUserById(id);
        if (user.isPresent()) {
            // Convertir Utilisateur en UserDTO
            UserDTO userDTO = UserMapper.toDTO(user.get());
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return utilisateurService.getAllUsers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Mise à jour d'un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<Utilisateur> existingUser = utilisateurService.getUserById(id);
        if (existingUser.isPresent()) {
            Utilisateur userToUpdate = existingUser.get();
            userToUpdate.setUsername(userDTO.getUsername());
            userToUpdate.setPassword(userDTO.getPassword());
            userToUpdate.setNom(userDTO.getNom());
            userToUpdate.setPrenom(userDTO.getPrenom());
            userToUpdate.setMail(userDTO.getMail());
            userToUpdate.setRole(userDTO.getRole());
            
            Utilisateur updatedUser = utilisateurService.updateUser(id, userToUpdate);
            
            // Convertir Utilisateur en UserDTO pour le retour
            UserDTO updatedUserDTO = UserMapper.toDTO(updatedUser);
            return ResponseEntity.ok(updatedUserDTO);
        }
        return ResponseEntity.notFound().build();
    }

    // Suppression d'un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<Utilisateur> user = utilisateurService.getUserById(id);
        if (user.isPresent()) {
            utilisateurService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
