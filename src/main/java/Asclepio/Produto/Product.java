package Asclepio.Produto;

import jakarta.persistence.*;
import Asclepio.Categoria.Categoria;
import Asclepio.ProdutoVariacao.ProdutoVariacao;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", name = "imagem_base64")
    private String imagemBase64;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVariacao> variacoes = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String name, String imagemBase64, Categoria categoria) {
        this.id = id;
        this.name = name;
        this.imagemBase64 = imagemBase64;
        this.categoria = categoria;
    }

    public Product(String name, String imagemBase64, Categoria categoria) {
        this(null, name, imagemBase64, categoria);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagemBase64() {
        return imagemBase64;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<ProdutoVariacao> getVariacoes() {
        return variacoes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setVariacoes(List<ProdutoVariacao> variacoes) {
        this.variacoes = variacoes;
    }
}