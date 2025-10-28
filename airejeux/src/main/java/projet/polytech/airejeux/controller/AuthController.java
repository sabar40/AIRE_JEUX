package projet.polytech.airejeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Repository.UtilisateurRepository;
import projet.polytech.airejeux.Service.JwtService;
import projet.polytech.airejeux.dto.UserDTO;
import projet.polytech.airejeux.mapper.UserMapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🧩 Inscription
    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        Utilisateur user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        utilisateurRepository.save(user);
        return "Utilisateur enregistré avec succès ✅";
    }

    // 🔐 Connexion
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );
        Utilisateur utilisateur = utilisateurRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return jwtService.generateToken(utilisateur);
    }
}
