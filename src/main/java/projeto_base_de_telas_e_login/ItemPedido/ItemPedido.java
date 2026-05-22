package projeto_base_de_telas_e_login.ItemPedido;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.Pedido.Pedido;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // snapshot do produto
    private Long produtoId;

    private String nomeProduto;

    private String variacao;

    @Column(columnDefinition = "TEXT")
    private String imagemUrl;

    private String categoria;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    private Integer quantidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal percentualDesconto;


    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public ItemPedido() {
    }

    public ItemPedido(
            Long id,
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

        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.variacao = variacao;
        this.imagemUrl = imagemUrl;
        this.categoria = categoria;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.pedido = pedido;

        this.percentualDesconto =
                percentualDesconto != null
                        ? percentualDesconto
                        : BigDecimal.ZERO;

        recalcularSubtotal();
    }

    private void recalcularSubtotal() {

        if (this.precoUnitario != null
                && this.quantidade != null) {

            BigDecimal desconto =
                    percentualDesconto != null
                            ? percentualDesconto
                            : BigDecimal.ZERO;

            BigDecimal precoComDesconto =
                    precoUnitario.subtract(
                            precoUnitario.multiply(desconto)
                                    .divide(BigDecimal.valueOf(100))
                    );

            this.subtotal =
                    precoComDesconto.multiply(
                            BigDecimal.valueOf(quantidade)
                    );
        }
    }
    // GETTERS

    public Long getId() {
        return id;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
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

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        this.quantidade = quantidade;
        recalcularSubtotal();
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}