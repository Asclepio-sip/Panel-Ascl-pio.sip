package Asclepio.Usuario.Permission.dto;

import Asclepio.Usuario.Permission.Permission;

import java.util.UUID;

public record PermissionResponseDTO(
        UUID id,
        String nome
) {
    public static PermissionResponseDTO fromEntity(Permission permission) {
        return new PermissionResponseDTO(
                permission.getId(),
                permission.getNome()
        );
    }
}