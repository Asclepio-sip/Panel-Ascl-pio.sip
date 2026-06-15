package Asclepio.Estoque;

import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.dto.EstoqueAddDto;
import Asclepio.Estoque.service.EstoqueMovimentacaoService;
import Asclepio.Estoque.service.EstoqueQueryService;
import Asclepio.Estoque.service.EstoqueValidator;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.ProdutoVariacao.ProdutoVariacaoRepository;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;
    private final EstoqueValidator validator;
    private final EstoqueQueryService queryService;
    private final EstoqueMovimentacaoService movimentacaoService;

    public EstoqueService(EstoqueRepository estoqueRepository, LojaRepository lojaRepository, ProdutoVariacaoRepository produtoVariacaoRepository, EstoqueValidator validator, EstoqueQueryService queryService, EstoqueMovimentacaoService movimentacaoService) {
        this.estoqueRepository = estoqueRepository;
        this.lojaRepository = lojaRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.validator = validator;
        this.queryService = queryService;
        this.movimentacaoService = movimentacaoService;
    }

    @Transactional
    public void criar(EstoqueAddDto dto) {

        validator.validarCriacao(dto);

        var loja = dto.lojaID() != null ? lojaRepository.findById(dto.lojaID()) : lojaRepository.findByNomeLoja(dto.nomeLoja());

        var lojaFinal = loja.orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        var variacaoFinal = produtoVariacaoRepository.findById(dto.variacaoId()).orElseThrow(() -> new ResourceNotFoundException("Variação não encontrada"));

        validator.validarEstoqueDuplicado(lojaFinal.getId(), variacaoFinal.getId());

        Estoque estoque = new Estoque(null, lojaFinal, variacaoFinal, dto.quantidade(), dto.precoVenda(), BigDecimal.ZERO);

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