package Asclepio.ProdutoVariacao.dto;

public record ProdutoVariacaoUpdateRequest(
        String nomeVariacao,
        String codigoBarras,
        String dosagem,
        String apresentacao,
        Boolean ativo
) {
}