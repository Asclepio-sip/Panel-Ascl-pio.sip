package Asclepio.Pedido.dto;

import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Enum.TipoAtendimentoPedido;

import java.time.LocalDate;

public record PedidoFiltro(
        Long lojaId,
        String nomeLoja,
        String nomeCliente,
        String telefone,
        String email,
        String bairro,
        StatusDoPedido status,
        TipoAtendimentoPedido tipoAtendimentoPedido,
        FormaDePagamento formaDePagamento,
        Boolean freteGratis,
        LocalDate dataInicio,
        LocalDate dataFim,
        Boolean somenteHoje

) {
}