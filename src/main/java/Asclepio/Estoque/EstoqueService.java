package Asclepio.Estoque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.Repository.EstoqueSpecification;
import Asclepio.Estoque.dto.EstoqueAddDto;
import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.Loja.Loja.LojaRepository;
import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.ProdutoVariacao.ProdutoVariacaoRepository;
import Asclepio.Usuario.User.User;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, LojaRepository lojaRepository, ProdutoVariacaoRepository produtoVariacaoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.estoqueRepository = estoqueRepository;
        this.lojaRepository = lojaRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @Transactional
    public void criar(EstoqueAddDto dto) {

        if (dto.lojaID() == null && dto.nomeLoja() == null) {
            throw new RuntimeException("Informe lojaID ou nomeLoja");
        }

        var loja = dto.lojaID() != null ? lojaRepository.findById(dto.lojaID()) : lojaRepository.findByNomeLoja(dto.nomeLoja());

        var lojaFinal = loja.orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        var variacao = produtoVariacaoRepository.findById(dto.variacaoId());

        var variacaoFinal = variacao.orElseThrow(() -> new RuntimeException("Variação não encontrada"));

        var produtoFinal = variacaoFinal.getProduto();

        var existente = estoqueRepository.findByLoja_IdAndProdutoVariacao_Id(lojaFinal.getId(), variacaoFinal.getId());

        if (existente.isPresent()) {
            throw new RuntimeException("Essa variação já existe no estoque");
        }

        Estoque estoque = new Estoque(null, lojaFinal, variacaoFinal, dto.quantidade(), dto.precoVenda(), BigDecimal.ZERO);

        estoqueRepository.save(estoque);

        movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(estoque, estoque.getLoja(), produtoFinal, variacaoFinal, usuarioLogado(), TipoMovimentacaoEstoque.CRIACAO, 0, estoque.getQuantidade(), BigDecimal.ZERO, estoque.getPrecoVenda(), BigDecimal.ZERO, estoque.getPercentualDesconto(), "Produto adicionado ao estoque"));
    }

    @Transactional
    public void atualizar(Long lojaId, Long variacaoId, Integer quantidade, BigDecimal precoVenda) {
        Estoque estoque = estoqueRepository.findByLoja_IdAndProdutoVariacao_Id(lojaId, variacaoId).orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        User usuario = usuarioLogado();

        Integer quantidadeAntes = estoque.getQuantidade();
        BigDecimal precoAntes = estoque.getPrecoVenda();
        BigDecimal descontoAntes = estoque.getPercentualDesconto();

        if (quantidade != null) {

            if (quantidade < 0) {
                throw new RuntimeException("Quantidade inválida");
            }

            estoque.setQuantidade(quantidade);
        }

        if (precoVenda != null) {

            if (precoVenda.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Preço inválido");
            }

            estoque.setPrecoVenda(precoVenda);
        }

        estoque.setAtualizadoEm(LocalDateTime.now());

        estoqueRepository.save(estoque);

        movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(estoque, estoque.getLoja(), estoque.getProdutoVariacao().getProduto(), estoque.getProdutoVariacao(), usuario, TipoMovimentacaoEstoque.ATUALIZACAO, quantidadeAntes, estoque.getQuantidade(), precoAntes, estoque.getPrecoVenda(), descontoAntes, estoque.getPercentualDesconto(), "Estoque atualizado manualmente"));
    }

    @Transactional
    public void deletar(Long id) {

        Estoque estoque = estoqueRepository.findById(id).orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(estoque, estoque.getLoja(), estoque.getProdutoVariacao().getProduto(), estoque.getProdutoVariacao(), usuarioLogado(), TipoMovimentacaoEstoque.DELECAO, estoque.getQuantidade(), 0, estoque.getPrecoVenda(), BigDecimal.ZERO, estoque.getPercentualDesconto(), BigDecimal.ZERO, "Estoque removido pelo usuário"));

        estoqueRepository.delete(estoque);
    }

    public List<Estoque> buscarPorLoja(Long lojaId) {
        return estoqueRepository.findByLoja_Id(lojaId);
    }

    @Transactional
    public void aplicarPromocao(Long lojaId, Long variacaoId, BigDecimal percentual) {

        if (percentual == null) {
            throw new RuntimeException("Percentual obrigatório");
        }

        if (percentual.compareTo(BigDecimal.ZERO) < 0 || percentual.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Percentual deve ser entre 0 e 100");
        }

        Estoque estoque = estoqueRepository.findByLoja_IdAndProdutoVariacao_Id(lojaId, variacaoId).orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        BigDecimal descontoAntes = estoque.getPercentualDesconto();

        estoque.setPercentualDesconto(percentual);
        estoque.setAtualizadoEm(LocalDateTime.now());

        estoqueRepository.save(estoque);

        String observacao = percentual.compareTo(BigDecimal.ZERO) == 0 ? "Promoção removida" : "Promoção aplicada de " + percentual + "%";

        movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(estoque, estoque.getLoja(), estoque.getProdutoVariacao().getProduto(), estoque.getProdutoVariacao(), usuarioLogado(), TipoMovimentacaoEstoque.PROMOCAO, estoque.getQuantidade(), estoque.getQuantidade(), estoque.getPrecoVenda(), estoque.getPrecoVenda(), descontoAntes, estoque.getPercentualDesconto(), observacao));
    }

    public Page<ListaDeEstoqueDasLojasResponse> listarTodos(EstoqueFiltro filtro, Pageable pageable) {
        return estoqueRepository.findAll(EstoqueSpecification.filtrar(filtro), pageable).map(ListaDeEstoqueDasLojasResponse::fromDomain);
    }

    private User usuarioLogado() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}