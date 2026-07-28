package Asclepio.CadastroProduto.dto;

import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;

public record CadastroProdutoCompletoResponse(

        ProdutoStorageResponse produto,

        ProdutoVariacaoResponseDTO variacao,

        Long estoqueId

) {
}