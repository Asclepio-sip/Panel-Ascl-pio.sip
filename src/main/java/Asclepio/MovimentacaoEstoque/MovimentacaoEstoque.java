package Asclepio.MovimentacaoEstoque;

import Asclepio.Estoque.Estoque;
import Asclepio.Loja.Loja.Loja;
import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.Usuario.User.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MOVIMENTACAO_ESTOQUE")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOV_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOV_ESTOQUE_ID")
    private Estoque estoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOV_LOJA_ID")
    private Loja loja;

    @Column(name = "MOV_PRODUTO_ID")
    private Long produtoId;

    @Column(name = "MOV_VARIACAO_ID")
    private Long produtoVariacaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOV_USUARIO_ID")
    private User usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "MOV_TIPO", nullable = false, length = 30)
    private TipoMovimentacaoEstoque tipo;

    @Column(name = "MOV_QUANTIDADE_ANTES")
    private Integer quantidadeAntes;

    @Column(name = "MOV_QUANTIDADE_DEPOIS")
    private Integer quantidadeDepois;

    @Column(name = "MOV_PRECO_ANTES", precision = 10, scale = 2)
    private BigDecimal precoAntes;

    @Column(name = "MOV_PRECO_DEPOIS", precision = 10, scale = 2)
    private BigDecimal precoDepois;

    @Column(name = "MOV_DESCONTO_ANTES", precision = 5, scale = 2)
    private BigDecimal descontoAntes;

    @Column(name = "MOV_DESCONTO_DEPOIS", precision = 5, scale = 2)
    private BigDecimal descontoDepois;

    @Column(name = "MOV_CRIADO_EM", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "MOV_OBSERVACAO", length = 500)
    private String observacao;

    protected MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(
            Estoque estoque,
            Loja loja,
            Long produtoId,
            Long produtoVariacaoId,
            User usuario,
            TipoMovimentacaoEstoque tipo,
            Integer quantidadeAntes,
            Integer quantidadeDepois,
            BigDecimal precoAntes,
            BigDecimal precoDepois,
            BigDecimal descontoAntes,
            BigDecimal descontoDepois,
            String observacao
    ) {
        this.estoque = estoque;
        this.loja = loja;
        this.produtoId = produtoId;
        this.produtoVariacaoId = produtoVariacaoId;
        this.usuario = usuario;
        this.tipo = tipo;
        this.quantidadeAntes = quantidadeAntes;
        this.quantidadeDepois = quantidadeDepois;
        this.precoAntes = precoAntes;
        this.precoDepois = precoDepois;
        this.descontoAntes = descontoAntes;
        this.descontoDepois = descontoDepois;
        this.observacao = observacao;
        this.criadoEm = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }

    public boolean alterouQuantidade() {
        return quantidadeAntes != null &&
                quantidadeDepois != null &&
                !quantidadeAntes.equals(quantidadeDepois);
    }

    public boolean alterouPreco() {
        return precoAntes != null &&
                precoDepois != null &&
                precoAntes.compareTo(precoDepois) != 0;
    }

    public boolean alterouDesconto() {
        return descontoAntes != null &&
                descontoDepois != null &&
                descontoAntes.compareTo(descontoDepois) != 0;
    }

    public Long getId() {
        return id;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public Loja getLoja() {
        return loja;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public Long getProdutoVariacaoId() {
        return produtoVariacaoId;
    }

    public User getUsuario() {
        return usuario;
    }

    public TipoMovimentacaoEstoque getTipo() {
        return tipo;
    }

    public Integer getQuantidadeAntes() {
        return quantidadeAntes;
    }

    public Integer getQuantidadeDepois() {
        return quantidadeDepois;
    }

    public BigDecimal getPrecoAntes() {
        return precoAntes;
    }

    public BigDecimal getPrecoDepois() {
        return precoDepois;
    }

    public BigDecimal getDescontoAntes() {
        return descontoAntes;
    }

    public BigDecimal getDescontoDepois() {
        return descontoDepois;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MovimentacaoEstoque movimentacao)) return false;

        return id != null && id.equals(movimentacao.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}