package projeto_base_de_telas_e_login.adapter.in.web.dto.Product;

import projeto_base_de_telas_e_login.domain.model.Preco.Preco;
import projeto_base_de_telas_e_login.domain.model.product.Product;

import java.math.BigDecimal;

public class ProductoResponseDto {

    private Long id;
    private String name;
    private String variacao;
    private String imagemBase64;
    private String categoriaNome;
    // CONSTRUTOR
    public ProductoResponseDto() {}

    public static ProductoResponseDto fromDomain(Product p) {
        ProductoResponseDto dto = new ProductoResponseDto();
        dto.id = p.getId();
        dto.name = p.getName();
        dto.variacao = p.getVariacao();
        dto.imagemBase64 = p.getImagemBase64();
        dto.categoriaNome = p.getCategoria() != null ? p.getCategoria().getNomeCategoria() : null;
        return dto;
    }

    // GETTERS
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getVariacao() { return variacao; }
    public String getImagemBase64() { return imagemBase64; }
    public String getCategoriaNome() { return categoriaNome; }
}