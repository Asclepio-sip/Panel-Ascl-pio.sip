package Asclepio.Produto.dto;

public record ProdutoUpdateRequest(
        String nome,
        String descricao,
        String marca,
        String imagemUrl,
        Long categoriaId
) {
}