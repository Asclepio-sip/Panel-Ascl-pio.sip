package Asclepio.CadastroProduto.CadastroProdutoService;

import Asclepio.CadastroProduto.dto.CadastroEstoqueDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.EstoqueService;
import Asclepio.Produto.Produto;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.ProdutoResponse;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroProdutoService {

    private final ProdutoService produtoService;
    private final ProdutoVariacaoService produtoVariacaoService;
    private final EstoqueService estoqueService;

    public CadastroProdutoService(
            ProdutoService produtoService,
            ProdutoVariacaoService produtoVariacaoService,
            EstoqueService estoqueService
    ) {
        this.produtoService = produtoService;
        this.produtoVariacaoService = produtoVariacaoService;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public CadastroProdutoCompletoResponse cadastrar(CadastroProdutoCompletoDTO dto) {

        // 1. Cria o produto localmente com upload de imagem
        Produto produtoEntidade = produtoService.criarComImagem(
                dto.nome(),
                dto.descricao(),
                dto.marca(),
                dto.categoriaId(),
                dto.imagem()
        );

        // Convertemos para o DTO de resposta do produto
        ProdutoResponse produtoResponse = ProdutoResponse.fromEntity(produtoEntidade);

        // 2. Cria a variação usando o ProdutoVariacaoRequest correto
        ProdutoVariacaoResponse variacaoResponse = produtoVariacaoService.criar(
                produtoEntidade.getId(),
                new ProdutoVariacaoRequest(
                        dto.nomeVariacao(),
                        dto.codigoBarras(),
                        null,
                        null
                )
        );

        // 3. Cadastra a variação criada no estoque da loja
        Estoque estoque = estoqueService.criar(
                new CadastroEstoqueDTO(
                        dto.lojaId(),
                        variacaoResponse.id(),
                        dto.quantidade(),
                        dto.precoVenda()
                )
        );

        // 4. Retorna a resposta consolidada
        return new CadastroProdutoCompletoResponse(
                produtoResponse,
                variacaoResponse,
                estoque.getId()
        );
    }
}