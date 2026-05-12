package projeto_base_de_telas_e_login.dto.Pedido;

import projeto_base_de_telas_e_login.model.Pedido.Enum.StatusDoPedido;

public record AtualizarStatusPedidoDTO (
        StatusDoPedido status
){
}