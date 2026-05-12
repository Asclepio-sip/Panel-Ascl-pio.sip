package projeto_base_de_telas_e_login.Categoria.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarCategoria(
        @NotBlank String nomeCategoria
) {}
