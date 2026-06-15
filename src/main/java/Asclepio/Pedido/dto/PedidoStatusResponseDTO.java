package Asclepio.Pedido.dto;

import Asclepio.Pedido.Enum.StatusDoPedido;

public record PedidoStatusResponseDTO(
        String codigoRastreio,
        StatusDoPedido status
) {}