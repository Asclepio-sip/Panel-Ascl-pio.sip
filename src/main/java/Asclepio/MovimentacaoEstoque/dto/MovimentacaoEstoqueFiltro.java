package Asclepio.MovimentacaoEstoque.dto;

import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;

import java.time.LocalDate;
import java.util.UUID;

public record MovimentacaoEstoqueFiltro(
        Long lojaId,
        Long produtoId,
        Long estoqueId,
        String nomeLoja,
        String nomeProduto,
        String usuario,
        UUID usuarioId,
        TipoMovimentacaoEstoque tipo,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}