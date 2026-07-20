package Asclepio.Usuario.User.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestCriarContaDTO(
        @NotBlank
        String login,
        @NotBlank
        String password,
        @NotBlank
        String email,
        String nomeEmpresa
) {

}
