package Asclepio.MovimentacaoEstoque.dto;

import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;

import java.time.LocalDate;

public record MovimentacaoEstoqueFiltro(
        Long lojaId,
        Long produtoId,
        Long estoqueId,
        String nomeLoja,
        String nomeProduto,
        String usuario,
        TipoMovimentacaoEstoque tipo,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}