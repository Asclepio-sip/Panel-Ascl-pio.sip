package Asclepio.Pedido.dto;

import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.OrigemPedido;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Enum.TipoEntrega;

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
        OrigemPedido origem,
        Boolean freteGratis,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}