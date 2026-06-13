package projeto_base_de_telas_e_login.Categoria.dto;

public record CriarCategoria(
        String nomeCategoria,
        String descricao,
        String icone,
        Long categoriaPaiId
) {
}