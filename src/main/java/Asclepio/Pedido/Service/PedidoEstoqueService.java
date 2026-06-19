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

            estoqueRepository.save(estoque);

            movimentacaoService.registrarSaidaPedido(
                    estoque,
                    quantidadeAntes,
                    precoAntes,
                    descontoAntes
            );
        }
    }
}