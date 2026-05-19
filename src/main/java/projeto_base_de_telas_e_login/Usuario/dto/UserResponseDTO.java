package projeto_base_de_telas_e_login.Usuario.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String login,
        String role,
        String password
) {
}
