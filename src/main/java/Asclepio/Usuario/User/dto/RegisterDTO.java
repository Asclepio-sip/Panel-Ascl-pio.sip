package Asclepio.Usuario.User.dto;


import Asclepio.UserLoja.UsuarioLojaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record RegisterDTO(

        @NotBlank
        String login,

        @NotBlank
        String password,

        String email,

        List<UUID> permissionIds,

        @NotEmpty
        List<UsuarioLojaDTO> lojas

) {}