package Asclepio.Categoria.dto;

public record CategoriaFiltro(
        String nome,
        Long categoriaPaiId,
        Boolean somentePrincipais,
        Boolean ativa
) {
}