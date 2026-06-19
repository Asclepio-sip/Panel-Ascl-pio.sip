package Asclepio.Produto.dto;

public record ProdutoStorageResponse(
        Long id,
        String nome,
        String descricao,
        String marca,
        String imagemUrl,
        Long categoriaId,
        String nomeCategoria
) {
}