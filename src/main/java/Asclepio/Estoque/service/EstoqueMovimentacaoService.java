package Asclepio.Estoque.service;

import Asclepio.Estoque.Estoque;
import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import Asclepio.Usuario.User.User;
import Asclepio.config.security.UsuarioAutenticado;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EstoqueMovimentacaoService {

    private final MovimentacaoEstoqueRepository repository;
    private final ProdutoVariacaoService produtoVariacaoService;

    public EstoqueMovimentacaoService(
            MovimentacaoEstoqueRepository repository,
            ProdutoVariacaoService produtoVariacaoService
    ) {
        this.repository = repository;
        this.produtoVariacaoService = produtoVariacaoService;
    }

    public void registrarCriacao(Estoque estoque) {

        buscarVariacao(estoque.getVariacaoId());

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                null,
                null,
                usuarioLogado(),
                TipoMovimentacaoEstoque.ENTRADA_NO_ESTOQUE,
                0,
                estoque.getQuantidade(),
                BigDecimal.ZERO,
                estoque.getPrecoVenda(),
                BigDecimal.ZERO,
                estoque.getPercentualDesconto(),
                "Produto adicionado ao estoque"
        );

        repository.save(movimentacao);
    }

    public void registrarAtualizacao(
            Estoque estoque,
            Integer quantidadeAntes,
            BigDecimal precoAntes,
            BigDecimal descontoAntes
    ) {

        buscarVariacao(estoque.getVariacaoId());

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                null,
                null,
                usuarioLogado(),
                TipoMovimentacaoEstoque.ATUALIZACAO,
                quantidadeAntes,
                estoque.getQuantidade(),
                precoAntes,
                estoque.getPrecoVenda(),
                descontoAntes,
                estoque.getPercentualDesconto(),
                "Estoque atualizado manualmente"
        );

        repository.save(movimentacao);
    }

    public void registrarDelecao(Estoque estoque) {

        buscarVariacao(estoque.getVariacaoId());

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                null,
                null,
                usuarioLogado(),
                TipoMovimentacaoEstoque.DELECAO,
                estoque.getQuantidade(),
                0,
                estoque.getPrecoVenda(),
                BigDecimal.ZERO,
                estoque.getPercentualDesconto(),
                BigDecimal.ZERO,
                "Estoque removido pelo usuário"
        );

        repository.save(movimentacao);
    }

    public void registrarPromocao(
            Estoque estoque,
            BigDecimal descontoAntes,
            String observacao
    ) {

        buscarVariacao(estoque.getVariacaoId());

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                null,
                null,
                usuarioLogado(),
                TipoMovimentacaoEstoque.PROMOCAO,
                estoque.getQuantidade(),
                estoque.getQuantidade(),
                estoque.getPrecoVenda(),
                estoque.getPrecoVenda(),
                descontoAntes,
                estoque.getPercentualDesconto(),
                observacao
        );

        repository.save(movimentacao);
    }

    public void registrarSaidaPedido(
            Estoque estoque,
            Integer quantidadeAntes,
            BigDecimal precoAntes,
            BigDecimal descontoAntes
    ) {

        buscarVariacao(estoque.getVariacaoId());

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                null,
                null,
                usuarioLogado(),
                TipoMovimentacaoEstoque.SAIDA_PEDIDO,
                quantidadeAntes,
                estoque.getQuantidade(),
                precoAntes,
                estoque.getPrecoVenda(),
                descontoAntes,
                estoque.getPercentualDesconto(),
                "Baixa automática por pedido"
        );

        repository.save(movimentacao);
    }

    private ProdutoVariacaoResponse buscarVariacao(Long variacaoId) {
        return produtoVariacaoService.buscarPorIdDTO(variacaoId);
    }

    private User usuarioLogado() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UsuarioAutenticado usuarioAutenticado) {
            return usuarioAutenticado.getUser();
        }

        return null;
    }
}