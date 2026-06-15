package Asclepio.ProdutoVariacao;

import org.springframework.stereotype.Service;
import Asclepio.Produto.Product;
import Asclepio.Produto.Repository.ProductRepository;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoAddDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoUpdateDTO;

import java.util.List;

@Service
public class ProdutoVariacaoService {

    private final ProdutoVariacaoRepository repository;

    private final ProductRepository productRepository;

    public ProdutoVariacaoService(ProdutoVariacaoRepository repository, ProductRepository productRepository) {

        this.repository = repository;

        this.productRepository = productRepository;
    }

    public ProdutoVariacaoResponseDTO criar(Long produtoId, ProdutoVariacaoAddDTO dto) {

        Product produto = productRepository.findById(produtoId)

                .orElseThrow(() ->

                        new RuntimeException("Produto não encontrado"));

        ProdutoVariacao variacao = new ProdutoVariacao();

        variacao.setProduto(produto);

        variacao.setNomeVariacao(dto.nomeVariacao());

        variacao.setCodigoBarras(dto.codigoBarras());

        variacao.setAtivo(true);

        repository.save(variacao);

        return ProdutoVariacaoResponseDTO.fromEntity(variacao);
    }

    public List<ProdutoVariacaoResponseDTO> listarPorProduto(Long produtoId) {

        return repository.findByProduto_Id(produtoId).stream().map(ProdutoVariacaoResponseDTO::fromEntity).toList();

    }

    public ProdutoVariacaoResponseDTO atualizar(Long id, ProdutoVariacaoUpdateDTO dto) {
        ProdutoVariacao variacao = repository.findById(id).orElseThrow(() -> new RuntimeException("Variação não encontrada"));

        if (dto.nomeVariacao() != null && !dto.nomeVariacao().isBlank()) {
            variacao.setNomeVariacao(dto.nomeVariacao());
        }

        if (dto.codigoBarras() != null) {
            variacao.setCodigoBarras(dto.codigoBarras());
        }

        if (dto.ativo() != null) {
            variacao.setAtivo(dto.ativo());
        }

        repository.save(variacao);

        return ProdutoVariacaoResponseDTO.fromEntity(variacao);
    }

    public void deletar(Long id) {
        ProdutoVariacao variacao = repository.findById(id).orElseThrow(() -> new RuntimeException("Variação não encontrada"));

        repository.delete(variacao);
    }

}