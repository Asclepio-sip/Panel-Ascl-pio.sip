package Asclepio.ProdutoVariacao;

import Asclepio.Produto.Product;
import Asclepio.Produto.Repository.ProductRepository;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoAddDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoUpdateDTO;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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

        validarCriacao(produtoId, dto);

        Product produto = buscarProduto(produtoId);

        if (repository.existsByProduto_IdAndNomeVariacaoIgnoreCase(produtoId, dto.nomeVariacao().trim())) {

            throw new BusinessException("Já existe uma variação com esse nome para este produto");
        }

        ProdutoVariacao variacao = new ProdutoVariacao();

        variacao.setProduto(produto);

        variacao.setNomeVariacao(dto.nomeVariacao().trim());

        variacao.setCodigoBarras(dto.codigoBarras());

        variacao.setAtivo(true);

        repository.save(variacao);

        return ProdutoVariacaoResponseDTO.fromEntity(variacao);
    }

    public List<ProdutoVariacaoResponseDTO> listarPorProduto(Long produtoId) {

        buscarProduto(produtoId);

        return repository.findByProduto_Id(produtoId)

                .stream()

                .map(ProdutoVariacaoResponseDTO::fromEntity)

                .toList();
    }

    public ProdutoVariacaoResponseDTO atualizar(Long id, ProdutoVariacaoUpdateDTO dto) {

        if (dto == null) {

            throw new BusinessException("Dados da variação são obrigatórios");
        }

        ProdutoVariacao variacao = buscarVariacao(id);

        if (dto.nomeVariacao() != null && !dto.nomeVariacao().isBlank()) {

            boolean existe = repository.existsByProduto_IdAndNomeVariacaoIgnoreCase(variacao.getProduto().getId(), dto.nomeVariacao().trim());

            if (existe && !dto.nomeVariacao().equalsIgnoreCase(variacao.getNomeVariacao())) {

                throw new BusinessException("Já existe uma variação com esse nome");
            }

            variacao.setNomeVariacao(dto.nomeVariacao().trim());
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

        ProdutoVariacao variacao = buscarVariacao(id);

        repository.delete(variacao);
    }

    // ==========================
    // MÉTODOS PRIVADOS
    // ==========================

    private Product buscarProduto(Long produtoId) {

        if (produtoId == null) {

            throw new BusinessException("Produto é obrigatório");
        }

        return productRepository

                .findById(produtoId)

                .orElseThrow(() ->

                        new ResourceNotFoundException("Produto não encontrado"));
    }

    private ProdutoVariacao buscarVariacao(Long id) {

        if (id == null) {

            throw new BusinessException("ID da variação é obrigatório");
        }

        return repository

                .findById(id)

                .orElseThrow(() ->

                        new ResourceNotFoundException("Variação não encontrada"));
    }

    private void validarCriacao(Long produtoId, ProdutoVariacaoAddDTO dto) {

        if (produtoId == null) {

            throw new BusinessException("Produto é obrigatório");
        }

        if (dto == null) {

            throw new BusinessException("Dados da variação são obrigatórios");
        }

        if (dto.nomeVariacao() == null || dto.nomeVariacao().isBlank()) {

            throw new BusinessException("Nome da variação é obrigatório");
        }
    }
}