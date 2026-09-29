package Asclepio.Usuario.User.dto;

import Asclepio.UserLoja.UsuarioLojaDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AtualizarLojasUsuarioDTO(
        @NotEmpty(message = "É necessário informar ao menos uma loja.")
        @Valid
        List<UsuarioLojaDTO> lojas
) {}
