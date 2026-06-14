package projeto_base_de_telas_e_login.Usuario.Permission.dto;

import projeto_base_de_telas_e_login.Usuario.Permission.Permission;

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