package projet.polytech.airejeux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.dto.UserDTO;

@Mapper(componentModel = "spring") 
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // DTO → Entity
    @Mapping(target = "role", defaultValue = "USER")
    Utilisateur toEntity(UserDTO dto);

    // Entity → DTO
    UserDTO toDTO(Utilisateur user);
}
