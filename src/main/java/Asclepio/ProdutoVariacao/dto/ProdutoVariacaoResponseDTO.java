package Asclepio.ProdutoVariacao.dto;

import Asclepio.ProdutoVariacao.ProdutoVariacao;

public record ProdutoVariacaoResponseDTO(

        Long id,

        Long produtoId,

        String nomeProduto,

        String nomeVariacao,

        String codigoBarras,

        Boolean ativo

) {

    public static ProdutoVariacaoResponseDTO fromEntity(
            ProdutoVariacao p
    ) {

        return new ProdutoVariacaoResponseDTO(

                p.getId(),

                p.getProduto().getId(),

                p.getProduto().getName(),

                p.getNomeVariacao(),

                p.getCodigoBarras(),

                p.getAtivo()

        );

    }

}