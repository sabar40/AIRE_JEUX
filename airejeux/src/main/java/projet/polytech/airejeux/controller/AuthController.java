package projet.polytech.airejeux.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.Repository.UtilisateurRepository;
import projet.polytech.airejeux.Service.JwtService;
import projet.polytech.airejeux.dto.UserDTO;
import projet.polytech.airejeux.mapper.UserMapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Injection par constructeur (propre et recommandé)
    public AuthController(
            AuthenticationManager authenticationManager,
            UtilisateurRepository utilisateurRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.utilisateurRepository = utilisateurRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // 🧩 Inscription
    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {

        Utilisateur user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        utilisateurRepository.save(user);
        return "Utilisateur enregistré avec succès ✅";
    }

    // 🔐 Connexion
    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        Utilisateur utilisateur = utilisateurRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return jwtService.generateToken(utilisateur);
    }
}
