package Asclepio.Estoque;

import Asclepio.CadastroProduto.dto.CadastroEstoqueDTO;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.dto.EstoqueAddDto;
import Asclepio.Estoque.service.EstoqueMovimentacaoService;
import Asclepio.Estoque.service.EstoqueQueryService;
import Asclepio.Estoque.service.EstoqueValidator;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Produto.Produto;
import Asclepio.Produto.ProdutoService;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import Asclepio.exception.ResourceNotFoundException;
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
    private final ProdutoVariacaoService produtoVariacaoService;
    private final ProdutoService produtoService;
    private final EmpresaContext empresaContext;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            LojaRepository lojaRepository,
            EstoqueValidator validator,
            EstoqueQueryService queryService,
            EstoqueMovimentacaoService movimentacaoService,
            ProdutoVariacaoService produtoVariacaoService,
            ProdutoService produtoService,
            EmpresaContext empresaContext
    ) {
        this.estoqueRepository = estoqueRepository;
        this.lojaRepository = lojaRepository;
        this.validator = validator;
        this.queryService = queryService;
        this.movimentacaoService = movimentacaoService;
        this.produtoVariacaoService = produtoVariacaoService;
        this.produtoService = produtoService;
        this.empresaContext = empresaContext;
    }

    @Transactional
    public void criar(EstoqueAddDto dto) {

        validator.validarCriacao(dto);

        Long empresaId = empresaContext.getEmpresaId();

        var lojaOptional = dto.lojaID() != null
                ? lojaRepository.findByIdAndEmpresa_Id(dto.lojaID(), empresaId)
                : lojaRepository.findByNomeLojaIgnoreCaseAndEmpresa_Id(dto.nomeLoja(), empresaId);

        var lojaFinal = lojaOptional.orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        // Busca direta da variação pelo ID via Service local
        ProdutoVariacaoResponse variacaoFinal = produtoVariacaoService.buscarPorIdDTO(dto.variacaoId());

        validator.validarEstoqueDuplicado(lojaFinal.getId(), variacaoFinal.id());

        // Busca direta do produto pelo ID via Service local
        Produto produto = produtoService.buscarPorId(variacaoFinal.produtoId());

        Estoque estoque = new Estoque(
                null,
                lojaFinal,
                variacaoFinal.id(),
                dto.quantidade(),
                dto.precoVenda(),
                BigDecimal.ZERO,
                produto.getImagemUrl()
        );

        Estoque estoqueSalvo = estoqueRepository.save(estoque);

        movimentacaoService.registrarCriacao(estoqueSalvo);
    }

    @Transactional
    public void atualizar(Long lojaId, Long variacaoId, Integer quantidade, BigDecimal precoVenda) {

        Long empresaId = empresaContext.getEmpresaId();

        Estoque estoque = queryService.buscarPorLojaEVariacao(lojaId, variacaoId, empresaId);

        Integer quantidadeAntes = estoque.getQuantidade();
        BigDecimal precoAntes = estoque.getPrecoVenda();
        BigDecimal descontoAntes = estoque.getPercentualDesconto();

        validator.validarQuantidade(quantidade);
        validator.validarPreco(precoVenda);

        if (quantidade != null) {
            estoque.atualizarQuantidade(quantidade);
        }

        if (precoVenda != null) {
            estoque.atualizarPrecoVenda(precoVenda);
        }

        estoque.setAtualizadoEm(LocalDateTime.now());

        estoqueRepository.save(estoque);

        movimentacaoService.registrarAtualizacao(estoque, quantidadeAntes, precoAntes, descontoAntes);
    }

    @Transactional
    public void deletar(Long id) {

        Long empresaId = empresaContext.getEmpresaId();

        Estoque estoque = queryService.buscarPorId(id, empresaId);
        movimentacaoService.registrarDelecao(estoque);

        estoqueRepository.delete(estoque);
    }

    @Transactional
    public void aplicarPromocao(Long lojaId, Long variacaoId, BigDecimal percentual) {

        validator.validarPercentualPromocao(percentual);

        Long empresaId = empresaContext.getEmpresaId();

        Estoque estoque = queryService.buscarPorLojaEVariacao(lojaId, variacaoId, empresaId);

        BigDecimal descontoAntes = estoque.getPercentualDesconto();

        estoque.aplicarPromocao(percentual);
        estoque.setAtualizadoEm(LocalDateTime.now());

        estoqueRepository.save(estoque);

        String observacao = percentual.compareTo(BigDecimal.ZERO) == 0
                ? "Promoção removida"
                : "Promoção aplicada de " + percentual + "%";

        movimentacaoService.registrarPromocao(estoque, descontoAntes, observacao);
    }

    @Transactional
    public Estoque criar(CadastroEstoqueDTO dto) {

        Long empresaId = empresaContext.getEmpresaId();

        validator.validarQuantidade(dto.quantidade());
        validator.validarPreco(dto.precoVenda());

        var loja = lojaRepository
                .findByIdAndEmpresa_Id(dto.lojaId(), empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        // Busca direta da variação pelo ID via Service local
        ProdutoVariacaoResponse variacao = produtoVariacaoService.buscarPorIdDTO(dto.variacaoId());

        validator.validarEstoqueDuplicado(loja.getId(), variacao.id());

        // Busca direta do produto pelo ID via Service local
        Produto produto = produtoService.buscarPorId(variacao.produtoId());

        Estoque estoque = new Estoque(
                null,
                loja,
                variacao.id(),
                dto.quantidade(),
                dto.precoVenda(),
                BigDecimal.ZERO,
                produto.getImagemUrl()
        );

        Estoque estoqueSalvo = estoqueRepository.save(estoque);

        movimentacaoService.registrarCriacao(estoqueSalvo);

        return estoqueSalvo;
    }
}