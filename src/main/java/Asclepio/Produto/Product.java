package Asclepio.Produto;

import Asclepio.Categoria.Categoria;
import Asclepio.ProdutoVariacao.ProdutoVariacao;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_PRODUTO", uniqueConstraints = {@UniqueConstraint(name = "UK_PRODUTO_NOME", columnNames = "PRO_NOME")})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "PRO_NOME", nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT", name = "PRO_IMAGEM_BASE64")
    private String ImagemUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRO_CATEGORIA_ID", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVariacao> variacoes = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String name, String imagemBase64, Categoria categoria) {

        validarNome(name);

        this.id = id;

        this.name = name.trim();

        this.ImagemUrl = imagemBase64;

        this.categoria = categoria;
    }

    public Product(String name, String imagemBase64, Categoria categoria) {

        this(null, name, imagemBase64, categoria);
    }

    // ==========================
    // REGRAS DE NEGÓCIO
    // ==========================

    public void alterarNome(String novoNome) {

        validarNome(novoNome);

        this.name = novoNome.trim();
    }

    public void alterarImagem(String imagemBase64) {

        this.ImagemUrl = imagemBase64;
    }

    public void alterarCategoria(Categoria categoria) {

        if (categoria == null) {

            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        this.categoria = categoria;
    }

    public boolean possuiVariacoes() {

        return !variacoes.isEmpty();
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (nome.length() > 150) {

            throw new IllegalArgumentException("Nome do produto deve ter no máximo 150 caracteres");
        }
    }

    // ==========================
    // GETTERS
    // ==========================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagemUrl() {
        return ImagemUrl;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<ProdutoVariacao> getVariacoes() {
        return variacoes;
    }

    // ==========================
    // SETTERS
    // ==========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        alterarNome(name);
    }

    public void setImagemUrl(String imagemUrl) {
        this.ImagemUrl = imagemUrl;
    }

    public void setCategoria(Categoria categoria) {
        alterarCategoria(categoria);
    }

    public void setVariacoes(List<ProdutoVariacao> variacoes) {
        this.variacoes = variacoes;
    }

    // ==========================
    // EQUALS E HASHCODE
    // ==========================

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Product product)) return false;

        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}