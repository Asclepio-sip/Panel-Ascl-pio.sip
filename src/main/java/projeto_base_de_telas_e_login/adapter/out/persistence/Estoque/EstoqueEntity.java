package projeto_base_de_telas_e_login.adapter.out.persistence.Estoque;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja.LojaEntity;
import projeto_base_de_telas_e_login.adapter.out.persistence.Product.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "estoque",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"loja_id", "produto_id"})
        }
)
public class EstoqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loja_id")
    private LojaEntity loja;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private ProductEntity produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentualDesconto;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    protected EstoqueEntity() {}

    public EstoqueEntity(
            LojaEntity loja,
            ProductEntity produto,
            Integer quantidade,
            BigDecimal precoVenda,
            BigDecimal percentualDesconto
    ) {
        this.loja = loja;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
        this.percentualDesconto = percentualDesconto != null ? percentualDesconto : BigDecimal.ZERO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LojaEntity getLoja() {
        return loja;
    }

    public ProductEntity getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
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

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
        this.atualizadoEm = LocalDateTime.now();
    }
}