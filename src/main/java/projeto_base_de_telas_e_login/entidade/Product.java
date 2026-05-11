package projeto_base_de_telas_e_login.entidade;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String variacao;

    @Lob
    @Column(name = "imagem_base64")
    private String imagemBase64;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Product() {
    }

    public Product(Long id, String name, String variacao, String imagemBase64, Categoria categoria) {
        this.id = id;
        this.name = name;
        this.variacao = variacao;
        this.imagemBase64 = imagemBase64;
        this.categoria = categoria;
    }

    public Product(String name, String variacao, String imagemBase64, Categoria categoria) {
        this(null, name, variacao, imagemBase64, categoria);
    }

    // GETTERS E SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariacao() {
        return variacao;
    }

    public void setVariacao(String variacao) {
        this.variacao = variacao;
    }

    public String getImagemBase64() {
        return imagemBase64;
    }

    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}