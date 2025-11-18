package projet.polytech.airejeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.Service.JeuxService;
import projet.polytech.airejeux.dto.JeuxRequestDto;
import projet.polytech.airejeux.dto.JeuxResponseDto;
import projet.polytech.airejeux.mapper.JeuxMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jeux") // Préfixe pour toutes les routes
@CrossOrigin(origins = "*")
public class JeuxController {

    @Autowired
    private JeuxService jeuxService;

    // --- ENDPOINTS POUR TOUS LES UTILISATEURS CONNECTÉS ---

    /**
     * GET /api/jeux
     * Récupère la liste de tous les jeux.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()") // N'importe qui de connecté peut voir les jeux
    public ResponseEntity<List<JeuxResponseDto>> getAllJeux() {
        List<Jeux> jeuxList = jeuxService.getAllJeux();
        // Conversion en liste de DTOs
        List<JeuxResponseDto> dtos = jeuxList.stream()
                .map(JeuxMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/jeux/{id}
     * Récupère un jeu spécifique par son ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JeuxResponseDto> getJeuById(@PathVariable Long id) {
        Jeux jeu = jeuxService.getJeuById(id);
        return ResponseEntity.ok(JeuxMapper.toDto(jeu));
    }

    // --- ENDPOINTS RÉSERVÉS AUX ADMINS ---

    /**
     * POST /api/jeux
     * Crée un nouveau jeu.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JeuxResponseDto> createJeu(@RequestBody JeuxRequestDto dto) {
        Jeux nouveauJeu = jeuxService.createJeu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(JeuxMapper.toDto(nouveauJeu));
    }

    /**
     * PUT /api/jeux/{id}
     * Met à jour un jeu existant.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JeuxResponseDto> updateJeu(@PathVariable Long id, @RequestBody JeuxRequestDto dto) {
        Jeux jeuMisAJour = jeuxService.updateJeu(id, dto);
        return ResponseEntity.ok(JeuxMapper.toDto(jeuMisAJour));
    }

    /**
     * DELETE /api/jeux/{id}
     * Supprime un jeu.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteJeu(@PathVariable Long id) {
        jeuxService.deleteJeu(id);
        return ResponseEntity.ok("Jeu (id: " + id + ") supprimé avec succès.");
    }
}