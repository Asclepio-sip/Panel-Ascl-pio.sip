package Asclepio.Produto.dto;


import Asclepio.Produto.Produto;

public record ProductoResponseDto (
        Long id,
        String name,
        String imagemBase64,
        String categoriaNome
){

    public static ProductoResponseDto fromEntity(Produto p) {

        return new ProductoResponseDto(
                p.getId(),
                p.getNome(),
                p.getImagemUrl(),
                p.getCategoria() != null
                        ? p.getCategoria().getNomeCategoria()
                        : null
        );
    }
}