package Asclepio.Usuario.User.dto;

import Asclepio.UserLoja.UsuarioLojaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.Valid;

import java.util.List;

public record RegisterDTO(
        @NotBlank
        String nome,
        @NotBlank
        String password,
        @NotBlank
        String email,
        @NotEmpty(message = "É necessário informar ao menos uma loja.")
        @Valid
        List<UsuarioLojaDTO> lojas
) {}