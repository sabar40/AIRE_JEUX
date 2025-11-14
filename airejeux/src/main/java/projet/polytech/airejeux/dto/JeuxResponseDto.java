package projet.polytech.airejeux.dto;

import lombok.Data;

@Data
public class JeuxResponseDto {
    private Long id; // Important: Long pour correspondre à l'entité
    private String nom;
    private int quantite;
    private String description;
    private CoordonneesDto coordonnees;
}