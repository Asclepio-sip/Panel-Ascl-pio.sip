package Asclepio.Pedido.dto;


import Asclepio.Pedido.Enum.OrigemPedido;
import Asclepio.Pedido.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListaDePedidoDTO (
     Long id,
     String nomeCliente,
     String status,
     LocalDateTime criadoEm,
     BigDecimal totalFinal,
     OrigemPedido origem
){

    public static ListaDePedidoDTO fromEntity(Pedido pedido) {
        return new ListaDePedidoDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getStatus().name(),
                pedido.getCriadoEm(),
                pedido.getTotalFinal(),
                pedido.getOrigem()
        );
    }

}