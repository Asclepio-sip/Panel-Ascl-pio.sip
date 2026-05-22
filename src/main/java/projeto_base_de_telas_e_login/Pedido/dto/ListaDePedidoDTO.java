package projeto_base_de_telas_e_login.Pedido.dto;


import projeto_base_de_telas_e_login.Pedido.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListaDePedidoDTO (
     Long id,
     String nomeCliente,
     String status,
     LocalDateTime criadoEm,
     BigDecimal totalFinal
){

    public static ListaDePedidoDTO fromEntity(Pedido pedido) {
        return new ListaDePedidoDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getStatus().name(),
                pedido.getCriadoEm(),
                pedido.getTotalFinal()
        );
    }

}