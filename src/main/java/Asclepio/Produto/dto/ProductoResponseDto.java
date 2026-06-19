package Asclepio.Produto.dto;

import Asclepio.Produto.Product;

public record ProductoResponseDto (
        Long id,
        String name,
        String imagemBase64,
        String categoriaNome
){

    public static ProductoResponseDto fromEntity(Product p) {

        return new ProductoResponseDto(
                p.getId(),
                p.getName(),
                p.getImagemUrl(),
                p.getCategoria() != null
                        ? p.getCategoria().getNomeCategoria()
                        : null
        );
    }
}