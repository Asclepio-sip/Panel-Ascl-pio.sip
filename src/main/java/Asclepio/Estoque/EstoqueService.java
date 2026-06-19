package Asclepio.Estoque;

import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.dto.EstoqueAddDto;
import Asclepio.Estoque.service.EstoqueMovimentacaoService;
import Asclepio.Estoque.service.EstoqueQueryService;
import Asclepio.Estoque.service.EstoqueValidator;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Produto.ProdutoStorageClient;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.ProdutoVariacao.ProdutoVariacaoStorageClient;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoFiltro;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueValidator validator;
    private final EstoqueQueryService queryService;
    private final EstoqueMovimentacaoService movimentacaoService;
    private final ProdutoVariacaoStorageClient produtoVariacaoClient;
    private final ProdutoStorageClient produtoStorageClient;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            LojaRepository lojaRepository,
            EstoqueValidator validator,
            EstoqueQueryService queryService,
            EstoqueMovimentacaoService movimentacaoService,
            ProdutoVariacaoStorageClient produtoVariacaoClient,
            ProdutoStorageClient produtoStorageClient
    ) {
        this.estoqueRepository = estoqueRepository;
        this.lojaRepository = lojaRepository;
        this.validator = validator;
        this.queryService = queryService;
        this.movimentacaoService = movimentacaoService;
        this.produtoVariacaoClient = produtoVariacaoClient;
        this.produtoStorageClient = produtoStorageClient;
    }

    @Transactional
    public void criar(EstoqueAddDto dto) {

        validator.validarCriacao(dto);

        var lojaOptional = dto.lojaID() != null
                ? lojaRepository.findById(dto.lojaID())
                : lojaRepository.findByNomeLoja(dto.nomeLoja());

        var lojaFinal = lojaOptional.orElseThrow(
                () -> new ResourceNotFoundException("Loja não encontrada")
        );

        ProdutoVariacaoFiltro filtro = new ProdutoVariacaoFiltro(
                dto.variacaoId(),
                null,
                null,
                null,
                null,
                true
        );

        var page = produtoVariacaoClient.listar(filtro, PageRequest.of(0, 1));

        if (page == null || page.content() == null || page.content().isEmpty()) {
            throw new ResourceNotFoundException("Variação não encontrada");
        }

        ProdutoVariacaoResponseDTO variacaoFinal = page.content().get(0);

        validator.validarEstoqueDuplicado(lojaFinal.getId(), variacaoFinal.id());

        ProdutoStorageResponse produto = produtoStorageClient.buscarPorId(
                variacaoFinal.produtoId()
        );

        Estoque estoque = new Estoque(
                null,
                lojaFinal,
                variacaoFinal.id(),
                dto.quantidade(),
                dto.precoVenda(),
                BigDecimal.ZERO,
                produto.imagemUrl()
        );

        estoqueRepository.save(estoque);

        movimentacaoService.registrarCriacao(estoque);
    }

    @Transactional
    public void atualizar(Long lojaId, Long variacaoId, Integer quantidade, BigDecimal precoVenda) {

        Estoque estoque = queryService.buscarPorLojaEVariacao(lojaId, variacaoId);

        Integer quantidadeAntes = estoque.getQuantidade();
        BigDecimal precoAntes = estoque.getPrecoVenda();
        BigDecimal descontoAntes = estoque.getPercentualDesconto();

        validator.validarQuantidade(quantidade);
        validator.validarPreco(precoVenda);

        if (quantidade != null) {
            estoque.setQuantidade(quantidade);
        }

        if (precoVenda != null) {
            estoque.setPrecoVenda(precoVenda);
        }

        estoque.setAtualizadoEm(LocalDateTime.now());

        estoqueRepository.save(estoque);

        movimentacaoService.registrarAtualizacao(estoque, quantidadeAntes, precoAntes, descontoAntes);
    }

    @Transactional
    public void deletar(Long id) {

        Estoque estoque = queryService.buscarPorId(id);

        movimentacaoService.registrarDelecao(estoque);

        estoqueRepository.delete(estoque);
    }

    @Transactional
    public void aplicarPromocao(Long lojaId, Long variacaoId, BigDecimal percentual) {

        validator.validarPercentualPromocao(percentual);

        Estoque estoque = queryService.buscarPorLojaEVariacao(lojaId, variacaoId);

        BigDecimal descontoAntes = estoque.getPercentualDesconto();

        estoque.setPercentualDesconto(percentual);
        estoque.setAtualizadoEm(LocalDateTime.now());

        estoqueRepository.save(estoque);

        String observacao = percentual.compareTo(BigDecimal.ZERO) == 0 ? "Promoção removida" : "Promoção aplicada de " + percentual + "%";

        movimentacaoService.registrarPromocao(estoque, descontoAntes, observacao);
    }
}