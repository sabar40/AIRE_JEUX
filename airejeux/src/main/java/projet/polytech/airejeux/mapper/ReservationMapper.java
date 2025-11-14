package projet.polytech.airejeux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import projet.polytech.airejeux.Entity.Reservation;
import projet.polytech.airejeux.dto.ReservationRequestDto;
import projet.polytech.airejeux.dto.ReservationResponseDto;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    // Mappe les champs de l'entité vers le DTO de réponse
    @Mapping(source = "utilisateur.id", target = "utilisateurId")
    @Mapping(source = "utilisateur.username", target = "utilisateurUsername")
    ReservationResponseDto toDto(Reservation entity);

    // Mappe le DTO de requête vers l'entité
    // On ignore 'utilisateur' car il n'est pas dans le DTO
    // On le définira manuellement dans le Service.
    @Mapping(target = "utilisateur", ignore = true)
    Reservation toEntity(ReservationRequestDto dto);
}