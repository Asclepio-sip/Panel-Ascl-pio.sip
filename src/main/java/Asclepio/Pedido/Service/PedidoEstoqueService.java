package Asclepio.Pedido.Service;

import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Estoque.service.EstoqueMovimentacaoService;
import Asclepio.Pedido.Pedido;
import Asclepio.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoEstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMovimentacaoService movimentacaoService;

    public PedidoEstoqueService(
            EstoqueRepository estoqueRepository,
            EstoqueMovimentacaoService movimentacaoService
    ) {
        this.estoqueRepository = estoqueRepository;
        this.movimentacaoService = movimentacaoService;
    }

    @Transactional
    public void baixarEstoqueDoPedido(Pedido pedido, List<Estoque> estoquesDaLoja) {

        for (var item : pedido.getItens()) {

            Estoque estoque = estoquesDaLoja.stream()
                    .filter(e -> e.getVariacaoId().equals(item.getVariacaoId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Estoque não encontrado para baixa"));

            Integer quantidadeAntes = estoque.getQuantidade();
            BigDecimal precoAntes = estoque.getPrecoVenda();
            BigDecimal descontoAntes = estoque.getPercentualDesconto();

            estoque.baixarEstoque(item.getQuantidade());

            item.registrarCustoUnitario(estoque.getPrecoCusto());

            estoqueRepository.save(estoque);

            movimentacaoService.registrarSaidaPedido(
                    estoque,
                    quantidadeAntes,
                    precoAntes,
                    descontoAntes
            );
        }
    }

    /**
     * Devolve à loja do pedido as quantidades de todos os itens (pedido cancelado).
     * Item cuja variação não tem mais estoque na loja (estoque excluído) fica de fora.
     */
    @Transactional
    public void devolverEstoqueDoPedido(Pedido pedido) {

        Long lojaId = pedido.getLoja().getId();

        for (var item : pedido.getItens()) {

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                continue;
            }

            Optional<Estoque> encontrado = estoqueRepository.findByLoja_IdAndVariacaoId(lojaId, item.getVariacaoId());
            if (encontrado.isEmpty()) {
                continue;
            }

            Estoque estoque = encontrado.get();
            Integer quantidadeAntes = estoque.getQuantidade();

            estoque.reporEstoque(item.getQuantidade());
            estoqueRepository.save(estoque);

            movimentacaoService.registrarDevolucaoPedido(estoque, quantidadeAntes, pedido.getId());
        }
    }
}