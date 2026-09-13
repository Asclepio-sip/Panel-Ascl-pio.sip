package Asclepio.ProdutoVariacao.dto;


import Asclepio.ProdutoVariacao.ProdutoVariacao;

public record ProdutoVariacaoResponse(
        Long id,
        Long produtoId,
        String nomeProduto,
        String nomeVariacao,
        String codigoBarras,
        Boolean ativo
) {

    public static ProdutoVariacaoResponse fromEntity(ProdutoVariacao variacao) {
        return new ProdutoVariacaoResponse(
                variacao.getId(),
                variacao.getProduto().getId(),
                variacao.getProduto().getNome(),
                variacao.getNomeVariacao(),
                variacao.getCodigoBarras(),
                variacao.getAtivo()
        );
    }
}