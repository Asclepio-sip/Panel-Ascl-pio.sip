package projeto_base_de_telas_e_login.Estoque;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.Loja.Loja.Loja;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.ProdutoVariacao.ProdutoVariacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque", uniqueConstraints = {@UniqueConstraint(columnNames = {"loja_id", "produto_variacao_id"})})
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loja_id")
    private Loja loja;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_variacao_id")
    private ProdutoVariacao produtoVariacao;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentualDesconto;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    protected Estoque() {
    }

    public Estoque(Long id, Loja loja, ProdutoVariacao produtoVariacao, Integer quantidade, BigDecimal precoVenda, BigDecimal percentualDesconto) {
        this.id = id;
        this.loja = loja;
        this.produtoVariacao = produtoVariacao;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
        this.percentualDesconto = percentualDesconto != null ? percentualDesconto : BigDecimal.ZERO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void baixarEstoque(Integer quantidadeVendida) {

        if (quantidadeVendida == null || quantidadeVendida <= 0) {

            throw new RuntimeException("Quantidade inválida");
        }

        if (this.quantidade < quantidadeVendida) {

            throw new RuntimeException("Estoque insuficiente");
        }

        this.quantidade -= quantidadeVendida;

        this.atualizadoEm = LocalDateTime.now();
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Long getId() {
        return id;
    }

    public Loja getLoja() {
        return loja;
    }

    public ProdutoVariacao getProdutoVariacao() {
        return produtoVariacao;
    }
    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setProdutoVariacao(ProdutoVariacao produtoVariacao) {
        this.produtoVariacao = produtoVariacao;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}