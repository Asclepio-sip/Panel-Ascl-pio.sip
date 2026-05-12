package projeto_base_de_telas_e_login.dto.Product;

import jakarta.validation.constraints.NotBlank;
import projeto_base_de_telas_e_login.entidade.Product;
import projeto_base_de_telas_e_login.entidade.Categoria;

public record ProductoAddDto(
        @NotBlank
         String name,
         String variacao,
         String imagemBase64,
         @NotBlank
         String categoriaNome
) {
    public Product toDomain() {
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(this.categoriaNome);


        return new Product(
                this.name,
                this.variacao,
                this.imagemBase64,
                categoria
        );
    }
}