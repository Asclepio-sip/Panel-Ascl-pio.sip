package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido;

import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.StatusDoPedido;

public class AtualizarStatusPedidoDTO {

    private StatusDoPedido status;

    public StatusDoPedido getStatus() {
        return status;
    }

    public void setStatus(StatusDoPedido status) {
        this.status = status;
    }
}