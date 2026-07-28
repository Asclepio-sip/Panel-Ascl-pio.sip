package Asclepio.CadastroProduto.CadastroProdutoService;


import Asclepio.CadastroProduto.dto.CadastroEstoqueDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.EstoqueService;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoAddDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroProdutoService {


    private final ProdutoService produtoService;
    private final ProdutoVariacaoService produtoVariacaoService;
    private final EstoqueService estoqueService;


    public CadastroProdutoService(ProdutoService produtoService, ProdutoVariacaoService produtoVariacaoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.produtoVariacaoService = produtoVariacaoService;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public CadastroProdutoCompletoResponse cadastrar(CadastroProdutoCompletoDTO dto){

        ProdutoStorageResponse produto =
                produtoService.criarComStorage(
                        dto.nome(),
                        dto.descricao(),
                        dto.marca(),
                        dto.categoriaId(),
                        dto.imagem()
                );

        ProdutoVariacaoResponseDTO variacao =
                produtoVariacaoService.criar(
                        produto.id(),
                        new ProdutoVariacaoAddDTO(
                                dto.nomeVariacao(),
                                dto.codigoBarras()
                        )
                );

        Estoque estoque = estoqueService.criar(
                new CadastroEstoqueDTO(
                        dto.lojaId(),
                        variacao.id(),
                        dto.quantidade(),
                        dto.precoVenda()
                )
        );

        return new CadastroProdutoCompletoResponse(
                produto,
                variacao,
                estoque.getId()
        );
    }
}
