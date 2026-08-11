package Asclepio.Loja.FormaPagamento;

import Asclepio.Pedido.Enum.FormaDePagamento;

public record LojaFormaPagamentoDTO(
        FormaDePagamento formaPagamento,
        String descricao,
        Boolean ativo
) {
}