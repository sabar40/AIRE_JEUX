package projet.polytech.airejeux.mapper;

import projet.polytech.airejeux.Entity.Coordonnees;
import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.dto.CoordonneesDto;
import projet.polytech.airejeux.dto.JeuxRequestDto;
import projet.polytech.airejeux.dto.JeuxResponseDto;

public class JeuxMapper {

    // --- DTO vers Entité ---

    public static Jeux toEntity(JeuxRequestDto dto) {
        Jeux jeu = new Jeux();
        jeu.setNom(dto.getNom());
        jeu.setQuantite(dto.getQuantite());
        jeu.setDescription(dto.getDescription());
        jeu.setCoordonnees(toEntity(dto.getCoordonnees()));
        return jeu;
    }

    public static Coordonnees toEntity(CoordonneesDto dto) {
        if (dto == null) return null;
        Coordonnees coordonnees = new Coordonnees();
        coordonnees.setLatitude(dto.getLatitude());
        coordonnees.setLongitude(dto.getLongitude());
        return coordonnees;
    }

    // --- Entité vers DTO ---

    public static JeuxResponseDto toDto(Jeux entity) {
        JeuxResponseDto dto = new JeuxResponseDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setQuantite(entity.getQuantite());
        dto.setDescription(entity.getDescription());
        dto.setCoordonnees(toDto(entity.getCoordonnees()));
        return dto;
    }

    public static CoordonneesDto toDto(Coordonnees entity) {
        if (entity == null) return null;
        CoordonneesDto dto = new CoordonneesDto();
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        return dto;
    }
}