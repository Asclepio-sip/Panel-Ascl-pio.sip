package projeto_base_de_telas_e_login.persistence.ItemPedido;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.persistence.Pedido.PedidoEntity;
import projeto_base_de_telas_e_login.model.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.model.Preco.Preco;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item")
public class ItemPedidoEntity {

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
    private PedidoEntity pedido;

    protected ItemPedidoEntity() {}

    public ItemPedidoEntity(ItemPedido item, PedidoEntity pedido) {
        this.nomeProduto = item.getNomeProduto();
        this.precoUnitario = item.getPrecoUnitario().getValor();
        this.quantidade = item.getQuantidade();
        this.subtotal = item.getSubtotal().getValor();
        this.pedido = pedido;
    }

    // ENTITY → DOMAIN
    public ItemPedido toDomain() {
        return new ItemPedido(
                this.nomeProduto,
                new Preco(this.precoUnitario),
                this.quantidade
        );
    }
}