package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido;

import projeto_base_de_telas_e_login.domain.model.ItemPedido.ItemPedido;

public record ItemPedidoDTO(
        Long produtoId,
        String nomeProduto,
        String variacao,
        String imagemUrl,
        String categoria,
        String preco,
        Integer quantidade
) {
    public static ItemPedidoDTO fromDomain(ItemPedido item) {

        return new ItemPedidoDTO(
                null,
                item.getNomeProduto(),
                null,
                null,
                null,
                item.getPrecoUnitario().toString(),
                item.getQuantidade()
        );
    }
}