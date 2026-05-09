package projeto_base_de_telas_e_login.adapter.out.persistence.Product;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.adapter.out.persistence.categoria.CategoriaEntity;
import projeto_base_de_telas_e_login.domain.model.Preco.Preco;
import projeto_base_de_telas_e_login.domain.model.product.Product;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String variacao;

    // 🔥 AGORA BASE64
    @Lob
    @Column(name = "imagem_base64")
    private String imagemBase64;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoria;


    protected ProductEntity() {}

    // DOMAIN → ENTITY
    public ProductEntity(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.variacao = product.getVariacao();
        this.imagemBase64 = product.getImagemBase64();
        this.categoria = new CategoriaEntity(product.getCategoria());

    }

    // ENTITY → DOMAIN
    public Product toDomain() {
        return new Product(
                this.id,
                this.name,
                this.variacao,
                this.imagemBase64,
                this.categoria.toDomain()

        );
    }

    // GETTERS
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getVariacao() { return variacao; }
    public String getImagemBase64() { return imagemBase64; }
    public CategoriaEntity getCategoria() { return categoria; }
}