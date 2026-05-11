package projeto_base_de_telas_e_login.dto.Pedido;


import projeto_base_de_telas_e_login.entidade.Product;

public record ProductListaDto(
        Long id,
        String nome,
        String variacao,
        String imagemUrl
) {

    public static ProductListaDto fromDomain(Product product) {
        return new ProductListaDto(
                product.getId(),
                product.getName(),
                product.getVariacao(),
                product.getImagemBase64()
        );
    }
}