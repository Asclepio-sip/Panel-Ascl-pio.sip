package projeto_base_de_telas_e_login.dto.Pedido;


import projeto_base_de_telas_e_login.persistence.Pedido.Pedido;

import java.time.LocalDateTime;

public record ListaDePedidoDTO (
     Long id,
     String nomeCliente,
     String status,
      LocalDateTime criadoEm){

    public static ListaDePedidoDTO fromEntity(Pedido pedido) {
        return new ListaDePedidoDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getStatus().name(),
                pedido.getCriadoEm()
        );
    }

}