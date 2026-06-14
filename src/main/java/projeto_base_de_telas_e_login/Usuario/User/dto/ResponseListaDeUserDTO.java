package projeto_base_de_telas_e_login.Usuario.User.dto;

import projeto_base_de_telas_e_login.Usuario.Role.dto.RoleResponseDTO;
import projeto_base_de_telas_e_login.Usuario.User.User;

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