package projet.polytech.airejeux.mapper;

import org.mapstruct.Mapper;
import projet.polytech.airejeux.Entity.Jeux;
import projet.polytech.airejeux.dto.JeuxRequestDto;
import projet.polytech.airejeux.dto.JeuxResponseDto;

// componentModel="spring" dit à MapStruct de générer un Bean Spring (@Component)
@Mapper(componentModel = "spring")
public interface JeuxMapper {

    // MapStruct gère automatiquement les DTOs imbriqués (CoordonneesDto)
    Jeux toEntity(JeuxRequestDto dto);
    
    JeuxResponseDto toDto(Jeux entity);
}