package Asclepio.Categoria.dto;

public record CriarCategoria(
        String nomeCategoria,
        String descricao,
        String icone,
        Long categoriaPaiId
) {
}