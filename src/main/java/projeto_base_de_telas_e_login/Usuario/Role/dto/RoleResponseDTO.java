package projeto_base_de_telas_e_login.Usuario.Role.dto;

import projeto_base_de_telas_e_login.Usuario.Permission.dto.PermissionResponseDTO;
import projeto_base_de_telas_e_login.Usuario.Role.Role;

import java.util.List;
import java.util.UUID;

public record RoleResponseDTO(
        UUID id,
        String nome,
        String descricao,
        List<PermissionResponseDTO> permissions
) {
    public static RoleResponseDTO fromEntity(Role role) {
        return new RoleResponseDTO(
                role.getId(),
                role.getNome(),
                role.getDescricao(),
                role.getPermissions()
                        .stream()
                        .map(PermissionResponseDTO::fromEntity)
                        .toList()
        );
    }
}