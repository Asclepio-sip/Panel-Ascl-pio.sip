package Asclepio.Usuario.User.dto;

import Asclepio.Usuario.Role.dto.RoleResponseDTO;
import Asclepio.Usuario.User.User;

import java.util.UUID;

public record ResponseListaDeUserDTO(
        UUID id,
        String username,
        String email,
        boolean ativo,
        RoleResponseDTO role
) {
    public static ResponseListaDeUserDTO fromEntity(User user) {
        return new ResponseListaDeUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAtivo(),
                RoleResponseDTO.fromEntity(user.getRole())
        );
    }
}