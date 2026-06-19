package Asclepio.Estoque;

import Asclepio.Loja.Loja.Loja;
import Asclepio.exception.BusinessException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "TB_ESTOQUE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_EST_LOJA_VARIACAO",
                        columnNames = {"EST_LOJA_ID", "EST_VARIACAO_ID"}
                )
        }
)
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EST_LOJA_ID", nullable = false)
    private Loja loja;

    @Column(name = "EST_VARIACAO_ID", nullable = false)
    private Long variacaoId;

    @Column(name = "EST_QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "EST_PRECO_VENDA", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "EST_PERCENTUAL_DESCONTO", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualDesconto = BigDecimal.ZERO;

    @Column(name = "EST_ATUALIZADO_EM", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "EST_IMAGEM_URL", nullable = false)
    private String imagemUrl;

    protected Estoque() {
    }

    public Estoque(
            Long id,
            Loja loja,
            Long variacaoId,
            Integer quantidade,
            BigDecimal precoVenda,
            BigDecimal percentualDesconto,
            String imagemUrl
    ) {
        this.id = id;
        this.loja = loja;
        this.variacaoId = variacaoId;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
        this.percentualDesconto = percentualDesconto != null
                ? percentualDesconto
                : BigDecimal.ZERO;
        this.atualizadoEm = LocalDateTime.now();
        this.imagemUrl =  imagemUrl;
    }

    @PrePersist
    public void prePersist() {
        if (this.atualizadoEm == null) {
            this.atualizadoEm = LocalDateTime.now();
        }

        if (this.percentualDesconto == null) {
            this.percentualDesconto = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();

        if (this.percentualDesconto == null) {
            this.percentualDesconto = BigDecimal.ZERO;
        }
    }

    public void baixarEstoque(Integer quantidadeVendida) {

        if (quantidadeVendida == null || quantidadeVendida <= 0) {
            throw new BusinessException("Quantidade inválida");
        }

        if (!possuiEstoque(quantidadeVendida)) {
            throw new BusinessException("Estoque insuficiente");
        }

        this.quantidade -= quantidadeVendida;
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean possuiEstoque(Integer quantidadeSolicitada) {
        return quantidadeSolicitada != null
                && quantidadeSolicitada > 0
                && this.quantidade >= quantidadeSolicitada;
    }

    public BigDecimal getValorFinal() {

        if (precoVenda == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal desconto = percentualDesconto == null
                ? BigDecimal.ZERO
                : percentualDesconto;

        BigDecimal valorDesconto = precoVenda
                .multiply(desconto)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return precoVenda.subtract(valorDesconto);
    }

    public boolean possuiPromocao() {
        return percentualDesconto != null
                && percentualDesconto.compareTo(BigDecimal.ZERO) > 0;
    }

    public void aplicarPromocao(BigDecimal percentual) {
        this.percentualDesconto = percentual != null
                ? percentual
                : BigDecimal.ZERO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void removerPromocao() {
        this.percentualDesconto = BigDecimal.ZERO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Loja getLoja() {
        return loja;
    }

    public Long getVariacaoId() {
        return variacaoId;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setVariacaoId(Long variacaoId) {
        this.variacaoId = variacaoId;
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

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Estoque estoque)) {
            return false;
        }

        return id != null && id.equals(estoque.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}