package projeto_base_de_telas_e_login.dto.Categoria;

import jakarta.validation.constraints.NotBlank;

public record CriarCategoriaDTO(
        @NotBlank String nomeCategoria
) {}
