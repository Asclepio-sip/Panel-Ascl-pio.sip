package Asclepio.Estoque.service;

import Asclepio.Estoque.Estoque;
import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.Usuario.User.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EstoqueMovimentacaoService {

    private final MovimentacaoEstoqueRepository repository;

    public EstoqueMovimentacaoService(MovimentacaoEstoqueRepository repository) {
        this.repository = repository;
    }

    public void registrarCriacao(Estoque estoque) {
        repository.save(new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                estoque.getProdutoVariacao().getProduto(),
                estoque.getProdutoVariacao(),
                usuarioLogado(),
                TipoMovimentacaoEstoque.CRIACAO,
                0,
                estoque.getQuantidade(),
                BigDecimal.ZERO,
                estoque.getPrecoVenda(),
                BigDecimal.ZERO,
                estoque.getPercentualDesconto(),
                "Produto adicionado ao estoque"
        ));
    }

    public void registrarAtualizacao(
            Estoque estoque,
            Integer quantidadeAntes,
            BigDecimal precoAntes,
            BigDecimal descontoAntes
    ) {
        repository.save(new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                estoque.getProdutoVariacao().getProduto(),
                estoque.getProdutoVariacao(),
                usuarioLogado(),
                TipoMovimentacaoEstoque.ATUALIZACAO,
                quantidadeAntes,
                estoque.getQuantidade(),
                precoAntes,
                estoque.getPrecoVenda(),
                descontoAntes,
                estoque.getPercentualDesconto(),
                "Estoque atualizado manualmente"
        ));
    }

    public void registrarDelecao(Estoque estoque) {
        repository.save(new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                estoque.getProdutoVariacao().getProduto(),
                estoque.getProdutoVariacao(),
                usuarioLogado(),
                TipoMovimentacaoEstoque.DELECAO,
                estoque.getQuantidade(),
                0,
                estoque.getPrecoVenda(),
                BigDecimal.ZERO,
                estoque.getPercentualDesconto(),
                BigDecimal.ZERO,
                "Estoque removido pelo usuário"
        ));
    }

    public void registrarPromocao(Estoque estoque, BigDecimal descontoAntes, String observacao) {
        repository.save(new MovimentacaoEstoque(
                estoque,
                estoque.getLoja(),
                estoque.getProdutoVariacao().getProduto(),
                estoque.getProdutoVariacao(),
                usuarioLogado(),
                TipoMovimentacaoEstoque.PROMOCAO,
                estoque.getQuantidade(),
                estoque.getQuantidade(),
                estoque.getPrecoVenda(),
                estoque.getPrecoVenda(),
                descontoAntes,
                estoque.getPercentualDesconto(),
                observacao
        ));
    }

    private User usuarioLogado() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}