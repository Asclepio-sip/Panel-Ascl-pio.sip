package projeto_base_de_telas_e_login.dto.Product;

import projeto_base_de_telas_e_login.model.product.Product;

public record ProductoResponseDto (

     Long id,
     String name,
     String variacao,
     String imagemBase64,
     String categoriaNome
){

    public static ProductoResponseDto fromDomain(Product p) {

        return new ProductoResponseDto(
                p.getId(),
                p.getName(),
                p.getVariacao(),
                p.getImagemBase64(),
                p.getCategoria() != null
                        ? p.getCategoria().getNomeCategoria()
                        : null
        );
    }

}