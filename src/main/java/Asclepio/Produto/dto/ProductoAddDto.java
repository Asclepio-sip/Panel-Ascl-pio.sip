package Asclepio.Produto.dto;

import jakarta.validation.constraints.NotBlank;
import Asclepio.Produto.Product;
import Asclepio.Categoria.Categoria;

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