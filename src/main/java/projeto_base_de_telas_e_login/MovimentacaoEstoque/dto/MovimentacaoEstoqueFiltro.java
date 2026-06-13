package projeto_base_de_telas_e_login.MovimentacaoEstoque.dto;

import projeto_base_de_telas_e_login.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;

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