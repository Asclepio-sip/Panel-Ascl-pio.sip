package Asclepio.CadastroProduto.dto;

import Asclepio.Produto.dto.ProdutoResponse;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponse;

public record CadastroProdutoCompletoResponse(
        ProdutoResponse produto,
        ProdutoVariacaoResponse variacao,
        Long estoqueId
) {}