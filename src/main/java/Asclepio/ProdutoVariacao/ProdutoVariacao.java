package Asclepio.ProdutoVariacao;

import jakarta.persistence.*;
import Asclepio.Produto.Product;

@Entity
@Table(name = "produto_variacao")
public class ProdutoVariacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeVariacao;

    private String codigoBarras;

    private Boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Product produto;

    public ProdutoVariacao() {
    }

    public ProdutoVariacao(Long id, String nomeVariacao, String codigoBarras, Boolean ativo, Product produto) {
        this.id = id;
        this.nomeVariacao = nomeVariacao;
        this.codigoBarras = codigoBarras;
        this.ativo = ativo != null ? ativo : true;
        this.produto = produto;
    }

    public ProdutoVariacao(String nomeVariacao, String codigoBarras, Product produto) {
        this(null, nomeVariacao, codigoBarras, true, produto);
    }

    public Long getId() {
        return id;
    }

    public String getNomeVariacao() {
        return nomeVariacao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Product getProduto() {
        return produto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeVariacao(String nomeVariacao) {
        this.nomeVariacao = nomeVariacao;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setProduto(Product produto) {
        this.produto = produto;
    }
}