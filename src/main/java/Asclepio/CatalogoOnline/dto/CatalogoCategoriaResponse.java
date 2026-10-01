package Asclepio.CatalogoOnline.dto;

import Asclepio.Categoria.Categoria;

// Resposta pública: só o necessário para montar a navbar/filtro de categorias.
public record CatalogoCategoriaResponse(
        Long id,
        String nomeCategoria,
        String icone,
        Long categoriaPaiId
) {

    public static CatalogoCategoriaResponse fromEntity(Categoria categoria) {
        return new CatalogoCategoriaResponse(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getIcone(),
                categoria.getCategoriaPai() != null ? categoria.getCategoriaPai().getId() : null
        );
    }
}
