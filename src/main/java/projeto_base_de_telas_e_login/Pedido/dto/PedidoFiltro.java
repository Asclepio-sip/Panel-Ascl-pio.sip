package projeto_base_de_telas_e_login.Pedido.dto;

import projeto_base_de_telas_e_login.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.Pedido.Enum.TipoEntrega;

import java.time.LocalDate;

public record PedidoFiltro(
        Long lojaId,
        String nomeLoja,

        String nomeCliente,
        String telefone,
        String email,
        String bairro,

        StatusDoPedido status,
        TipoEntrega tipoEntrega,
        FormaDePagamento formaDePagamento,

        Boolean freteGratis,

        LocalDate dataInicio,
        LocalDate dataFim
) {
}