package Asclepio.Produto.dto;

public record ProdutoFiltro(
        String nome,
        String variacao,
        Long categoriaId,
        String nomeCategoria
) {
}