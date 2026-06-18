package Asclepio.ProdutoVariacao.dto;

public record ProdutoVariacaoFiltro(
        Long produtoId,
        String nomeProduto,
        String nomeVariacao,
        String codigoBarras,
        Boolean ativo
) {
}