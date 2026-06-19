package Asclepio.ItemPedido;

import Asclepio.Pedido.Pedido;
import Asclepio.exception.BusinessException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "TB_ITEM_PEDIDO")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITP_ID")
    private Long id;

    @Column(name = "ITP_VARIACAO_ID", nullable = false)
    private Long variacaoId;

    @Column(name = "ITP_PRODUTO_ID", nullable = false)
    private Long produtoId;

    @Column(name = "ITP_NOME_PRODUTO", nullable = false, length = 200)
    private String nomeProduto;

    @Column(name = "ITP_VARIACAO", length = 100)
    private String variacao;

    @Column(name = "ITP_IMAGEM_URL", columnDefinition = "TEXT")
    private String imagemUrl;

    @Column(name = "ITP_CATEGORIA", length = 100)
    private String categoria;

    @Column(name = "ITP_PRECO_UNITARIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "ITP_QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "ITP_SUBTOTAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "ITP_PERCENTUAL_DESCONTO", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualDesconto = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ITP_PEDIDO_ID", nullable = false)
    private Pedido pedido;

    protected ItemPedido() {
    }

    public ItemPedido(
            Long id,
            Long variacaoId,
            Long produtoId,
            String nomeProduto,
            String variacao,
            String imagemUrl,
            String categoria,
            BigDecimal precoUnitario,
            Integer quantidade,
            Pedido pedido,
            BigDecimal percentualDesconto
    ) {
        validarQuantidade(quantidade);

        this.id = id;
        this.variacaoId = variacaoId;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.variacao = variacao;
        this.imagemUrl = imagemUrl;
        this.categoria = categoria;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.pedido = pedido;
        this.percentualDesconto = percentualDesconto != null
                ? percentualDesconto
                : BigDecimal.ZERO;

        recalcularSubtotal();
    }

    @PrePersist
    public void prePersist() {
        prepararDadosAntesDeSalvar();
    }

    @PreUpdate
    public void preUpdate() {
        prepararDadosAntesDeSalvar();
    }

    private void prepararDadosAntesDeSalvar() {
        if (percentualDesconto == null) {
            percentualDesconto = BigDecimal.ZERO;
        }

        validarQuantidade(quantidade);
        recalcularSubtotal();
    }

    private void recalcularSubtotal() {
        if (precoUnitario == null || quantidade == null) {
            subtotal = BigDecimal.ZERO;
            return;
        }

        subtotal = getPrecoFinalUnitario()
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPrecoFinalUnitario() {
        if (precoUnitario == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal desconto = percentualDesconto != null
                ? percentualDesconto
                : BigDecimal.ZERO;

        BigDecimal valorDesconto = precoUnitario
                .multiply(desconto)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return precoUnitario
                .subtract(valorDesconto)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean possuiDesconto() {
        return percentualDesconto != null
                && percentualDesconto.compareTo(BigDecimal.ZERO) > 0;
    }

    private void validarQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new BusinessException("Quantidade inválida");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getVariacaoId() {
        return variacaoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getVariacao() {
        return variacao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVariacaoId(Long variacaoId) {
        this.variacaoId = variacaoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setVariacao(String variacao) {
        this.variacao = variacao;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        recalcularSubtotal();
    }

    public void setQuantidade(Integer quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
        recalcularSubtotal();
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto != null
                ? percentualDesconto
                : BigDecimal.ZERO;

        recalcularSubtotal();
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ItemPedido itemPedido)) {
            return false;
        }

        return id != null && id.equals(itemPedido.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}