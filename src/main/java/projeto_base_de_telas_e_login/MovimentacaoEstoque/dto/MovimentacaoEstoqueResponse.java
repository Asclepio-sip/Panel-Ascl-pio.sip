package projeto_base_de_telas_e_login.MovimentacaoEstoque.dto;

import projeto_base_de_telas_e_login.MovimentacaoEstoque.MovimentacaoEstoque;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponse(
        Long id,

        Long estoqueId,

        Long lojaId,
        String nomeLoja,

        Long produtoId,
        String nomeProduto,

        String usuario,
        TipoMovimentacaoEstoque tipo,

        Integer quantidadeAntes,
        Integer quantidadeDepois,

        BigDecimal precoAntes,
        BigDecimal precoDepois,

        BigDecimal descontoAntes,
        BigDecimal descontoDepois,

        String observacao,

        LocalDateTime criadoEm
) {

    public static MovimentacaoEstoqueResponse fromEntity(MovimentacaoEstoque mov) {
        return new MovimentacaoEstoqueResponse(
                mov.getId(),

                mov.getEstoque() != null ? mov.getEstoque().getId() : null,

                mov.getLoja() != null ? mov.getLoja().getId() : null,
                mov.getLoja() != null ? mov.getLoja().getNomeLoja() : null,

                mov.getProduto() != null ? mov.getProduto().getId() : null,
                mov.getProduto() != null ? mov.getProduto().getName() : null,

                mov.getUsuario() != null ? mov.getUsuario().getUsername() : "Sistema",

                mov.getTipo(),

                mov.getQuantidadeAntes(),
                mov.getQuantidadeDepois(),

                mov.getPrecoAntes(),
                mov.getPrecoDepois(),

                mov.getDescontoAntes(),
                mov.getDescontoDepois(),

                mov.getObservacao(),

                mov.getCriadoEm()
        );
    }
}