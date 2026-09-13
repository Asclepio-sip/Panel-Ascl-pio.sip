package Asclepio.ProdutoVariacao.dto;

public record ProdutoVariacaoRequest(
        String nomeVariacao,
        String codigoBarras,
        String dosagem,
        String apresentacao
) {
}