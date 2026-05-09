package projeto_base_de_telas_e_login.adapter.out.persistence.Pedido.PDF.PdfTemplatePedido;

import java.math.BigDecimal;

public class ItemPedidoPdfDTO {

    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public ItemPedidoPdfDTO(String nomeProduto, Integer quantidade,
                             BigDecimal precoUnitario, BigDecimal subtotal) {
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }

    public String getNomeProduto() { return nomeProduto; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}