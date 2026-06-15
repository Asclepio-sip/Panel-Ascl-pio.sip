package Asclepio.Produto.dto;

public record ProdutoUpdateDto(
        String name,
        String variacao,
        String imagemBase64,
        String categoriaNome
) {}