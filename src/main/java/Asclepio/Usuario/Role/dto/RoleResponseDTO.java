package Asclepio.Usuario.Role.dto;

import Asclepio.Usuario.Role.Role;

import java.util.UUID;

public record RoleResponseDTO(
    UUID id,
    String nome,
    String descricao
) {
    public static RoleResponseDTO from(Role role) {
        return new RoleResponseDTO(
            role.getId(),
            role.getNome(),
            role.getDescricao()
        );
    }
}