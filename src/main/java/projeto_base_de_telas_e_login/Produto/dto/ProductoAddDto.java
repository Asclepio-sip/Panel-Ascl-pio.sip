package projeto_base_de_telas_e_login.Produto.dto;

import jakarta.validation.constraints.NotBlank;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.Categoria.Categoria;

public record ProductoAddDto(
        @NotBlank String name,
        String imagemBase64,
        @NotBlank String categoriaNome
) {
    public Product toDomain() {

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(this.categoriaNome);

        return new Product(
                this.name,
                this.imagemBase64,
                categoria
        );
    }
}