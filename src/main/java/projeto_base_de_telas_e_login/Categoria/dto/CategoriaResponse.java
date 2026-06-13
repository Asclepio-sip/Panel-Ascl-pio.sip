package projeto_base_de_telas_e_login.Categoria.dto;

import projeto_base_de_telas_e_login.Categoria.Categoria;

public record CategoriaResponse(
        Long id,
        String nomeCategoria,
        String descricao,
        String icone,
        Boolean ativa,
        Long categoriaPaiId,
        String nomeCategoriaPai
) {

    public static CategoriaResponse fromEntity(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getDescricao(),
                categoria.getIcone(),
                categoria.getAtiva(),
                categoria.getCategoriaPai() != null
                        ? categoria.getCategoriaPai().getId()
                        : null,
                categoria.getCategoriaPai() != null
                        ? categoria.getCategoriaPai().getNomeCategoria()
                        : null
        );
    }
}