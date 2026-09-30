package Asclepio.CatalogoOnline.dto;

import Asclepio.Estoque.Estoque;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponse;

import java.math.BigDecimal;

// Resposta pública: não expõe quantidade exata em estoque nem dados internos da loja.
public record CatalogoProdutoResponse(
        Long estoqueId,
        Long produtoId,
        Long variacaoId,
        String nomeProduto,
        String nomeVariacao,
        String imagemUrl,
        BigDecimal precoVenda,
        BigDecimal percentualDesconto,
        BigDecimal valorFinal,
        Boolean emPromocao
) {

    public static CatalogoProdutoResponse fromDomain(Estoque estoque, ProdutoVariacaoResponse variacao) {
        return new CatalogoProdutoResponse(
                estoque.getId(),
                variacao.produtoId(),
                variacao.id(),
                variacao.nomeProduto(),
                variacao.nomeVariacao(),
                estoque.getImagemUrl(),
                estoque.getPrecoVenda(),
                estoque.getPercentualDesconto(),
                estoque.getValorFinal(),
                estoque.possuiPromocao()
        );
    }
}
