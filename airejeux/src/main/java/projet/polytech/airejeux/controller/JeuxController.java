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
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jeux")
@CrossOrigin(origins = "*") 
public class JeuxController {

    @Autowired
    private JeuxService jeuxService;
    
    @Autowired
    private JeuxMapper jeuxMapper; // <-- INJECTION

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<JeuxResponseDto>> getAllJeux() {
        List<Jeux> jeuxList = jeuxService.getAllJeux();
        List<JeuxResponseDto> dtos = jeuxList.stream()
                .map(jeuxMapper::toDto) // <-- Utilise l'instance
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getJeuById(@PathVariable Long id) {
        try {
            Jeux jeu = jeuxService.getJeuById(id);
            return ResponseEntity.ok(jeuxMapper.toDto(jeu)); // <-- Utilise l'instance
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JeuxResponseDto> createJeu(@RequestBody JeuxRequestDto dto) {
        Jeux nouveauJeu = jeuxService.createJeu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jeuxMapper.toDto(nouveauJeu)); // <-- Utilise l'instance
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateJeu(@PathVariable Long id, @RequestBody JeuxRequestDto dto) {
        try {
            Jeux jeuMisAJour = jeuxService.updateJeu(id, dto);
            return ResponseEntity.ok(jeuxMapper.toDto(jeuMisAJour)); // <-- Utilise l'instance
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteJeu(@PathVariable Long id) {
        try {
            jeuxService.deleteJeu(id);
            return ResponseEntity.ok("Jeu (id: " + id + ") supprimé avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}