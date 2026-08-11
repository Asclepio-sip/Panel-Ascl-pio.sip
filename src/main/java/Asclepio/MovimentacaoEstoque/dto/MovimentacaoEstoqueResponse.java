package Asclepio.MovimentacaoEstoque.dto;

import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;

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

    public static MovimentacaoEstoqueResponse fromEntity(
            MovimentacaoEstoque mov
    ) {

        return new MovimentacaoEstoqueResponse(

                mov.getId(),

                mov.getEstoque() != null
                        ? mov.getEstoque().getId()
                        : null,

                mov.getLoja() != null
                        ? mov.getLoja().getId()
                        : null,

                mov.getLoja() != null
                        ? mov.getLoja().getNomeLoja()
                        : null,

                // agora vem direto do ID salvo
                mov.getProdutoId(),

                // como Produto está em outro microservice,
                // você não consegue pegar o nome daqui
                null,

                mov.getUsuario() != null
                        ? mov.getUsuario().getNome()
                        : "Sistema",

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