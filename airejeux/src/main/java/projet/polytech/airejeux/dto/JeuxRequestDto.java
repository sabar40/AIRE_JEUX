package projet.polytech.airejeux.dto;

import lombok.Data;

@Data
public class JeuxRequestDto { // Le nom de la classe correspond au nom du fichier
    private String nom;
    private int quantite;
    private String description;
    private CoordonneesDto coordonnees;
}