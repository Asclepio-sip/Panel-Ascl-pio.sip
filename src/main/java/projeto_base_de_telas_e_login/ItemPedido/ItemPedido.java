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

    private String nomeProduto;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    private Integer quantidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public ItemPedido() {
    }

    public ItemPedido(Long id, String nomeProduto, BigDecimal precoUnitario, Integer quantidade, Pedido pedido) {

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        this.id = id;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;

        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));

        this.pedido = pedido;
    }

    public Long getId() {
        return id;
    }

    public String getNomeProduto() {
        return nomeProduto;
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

    public void setQuantidade(Integer quantidade) {

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        this.quantidade = quantidade;

        this.subtotal = this.precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}