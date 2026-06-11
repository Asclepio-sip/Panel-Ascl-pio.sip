package projeto_base_de_telas_e_login.Usuario.User.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterDTO(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotNull
        UUID roleId) {
}