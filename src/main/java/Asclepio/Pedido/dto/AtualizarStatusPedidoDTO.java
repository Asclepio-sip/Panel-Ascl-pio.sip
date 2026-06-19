package Asclepio.Pedido.dto;

import Asclepio.Pedido.Enum.StatusDoPedido;

public record AtualizarStatusPedidoDTO (
        StatusDoPedido status
){
}