package projeto_base_de_telas_e_login.Produto.dto;

public record ProdutoFiltro(
        String nome,
        String variacao,
        Long categoriaId,
        String nomeCategoria
) {
}