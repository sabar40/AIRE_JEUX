package projet.polytech.airejeux.mapper;

import projet.polytech.airejeux.Entity.Utilisateur;
import projet.polytech.airejeux.dto.UserDTO;


public class UserMapper {

    public static Utilisateur toEntity(UserDTO dto) {
        Utilisateur user = new Utilisateur();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setMail(dto.getMail());
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER"); // rôle par défaut
        return user;
    }

    public static UserDTO toDTO(Utilisateur user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setMail(user.getMail());
        dto.setRole(user.getRole());
        return dto;
    }
}
