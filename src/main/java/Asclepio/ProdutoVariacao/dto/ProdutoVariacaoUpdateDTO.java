package Asclepio.ProdutoVariacao.dto;

public record ProdutoVariacaoUpdateDTO(
        String nomeVariacao,
        String codigoBarras,
        Boolean ativo
) {
}