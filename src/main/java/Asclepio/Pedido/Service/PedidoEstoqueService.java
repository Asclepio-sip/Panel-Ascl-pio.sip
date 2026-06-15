package Asclepio.Pedido.Service;

import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.Pedido.Pedido;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoEstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public PedidoEstoqueService(
            EstoqueRepository estoqueRepository,
            MovimentacaoEstoqueRepository movimentacaoEstoqueRepository
    ) {
        this.estoqueRepository = estoqueRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @Transactional
    public void baixarEstoqueDoPedido(Pedido pedido, List<Estoque> estoquesDaLoja) {

        for (var item : pedido.getItens()) {

            Estoque estoque = estoquesDaLoja.stream()
                    .filter(e -> e.getProdutoVariacao().getId().equals(item.getVariacaoId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Estoque não encontrado para baixa"));

            Integer quantidadeAntes = estoque.getQuantidade();
            BigDecimal precoAntes = estoque.getPrecoVenda();
            BigDecimal descontoAntes = estoque.getPercentualDesconto();

            estoque.baixarEstoque(item.getQuantidade());

            estoqueRepository.save(estoque);

            movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(
                    estoque,
                    estoque.getLoja(),
                    estoque.getProdutoVariacao().getProduto(),
                    estoque.getProdutoVariacao(),
                    null,
                    TipoMovimentacaoEstoque.SAIDA_PEDIDO,
                    quantidadeAntes,
                    estoque.getQuantidade(),
                    precoAntes,
                    estoque.getPrecoVenda(),
                    descontoAntes,
                    estoque.getPercentualDesconto(),
                    "Baixa automática por pedido"
            ));
        }
    }
}