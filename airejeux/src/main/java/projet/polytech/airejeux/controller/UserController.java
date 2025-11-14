package projet.polytech.airejeux.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final UtilisateurService utilisateurService;
    private final UserMapper userMapper;

    // Injection par constructeur (propre et recommandé)
    public UserController(UtilisateurService utilisateurService, UserMapper userMapper) {
        this.utilisateurService = utilisateurService;
        this.userMapper = userMapper;
    }

    // Création d'un utilisateur
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

        Utilisateur user = userMapper.toEntity(userDTO);
        Utilisateur savedUser = utilisateurService.createUser(user);

        UserDTO savedUserDTO = userMapper.toDTO(savedUser);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
    }

    // Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {

        return utilisateurService.getUserById(id)
                .map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer tous les utilisateurs
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return utilisateurService.getAllUsers().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Mise à jour d'un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {

        Optional<Utilisateur> existingUser = utilisateurService.getUserById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Utilisateur user = existingUser.get();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setMail(userDTO.getMail());
        user.setRole(userDTO.getRole());

        Utilisateur updatedUser = utilisateurService.updateUser(id, user);

        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    // Suppression d'un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        if (utilisateurService.getUserById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        utilisateurService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
