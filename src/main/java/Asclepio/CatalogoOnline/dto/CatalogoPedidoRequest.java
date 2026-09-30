package Asclepio.CatalogoOnline.dto;

import Asclepio.ItemPedido.DTO.ItemPedidoAddDTO;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.TipoAtendimentoPedido;
import Asclepio.Pedido.dto.pedido.PedidoAddDTO;

import java.util.List;

// Mesmo corpo do pedido online, sem lojaId: a loja vem da URL do catálogo.
public record CatalogoPedidoRequest(
        Long bairroId,
        String nomeCliente,
        String email,
        String telefone,
        String endereco,
        String complemento,
        String observacao,
        TipoAtendimentoPedido tipoAtendimentoPedido,
        List<ItemPedidoAddDTO> itens,
        FormaDePagamento formaDePagamento
) {

    public PedidoAddDTO toPedidoAddDTO(Long lojaId) {
        return new PedidoAddDTO(
                lojaId,
                bairroId,
                nomeCliente,
                email,
                telefone,
                endereco,
                complemento,
                observacao,
                tipoAtendimentoPedido,
                itens,
                formaDePagamento
        );
    }
}
