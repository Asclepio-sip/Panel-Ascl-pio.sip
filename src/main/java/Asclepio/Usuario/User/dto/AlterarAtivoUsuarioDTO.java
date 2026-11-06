package Asclepio.Usuario.User.dto;

import jakarta.validation.constraints.NotNull;

public record AlterarAtivoUsuarioDTO(
        @NotNull(message = "Informe se o usuário fica ativo ou não.")
        Boolean ativo
) {}
