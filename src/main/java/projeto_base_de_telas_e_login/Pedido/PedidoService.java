package projeto_base_de_telas_e_login.Pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairro;
import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairroRepository;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import projeto_base_de_telas_e_login.Pedido.Repository.PedidoRepository;
import projeto_base_de_telas_e_login.Pedido.dto.PedidoAddDTO;
import org.springframework.transaction.annotation.Transactional;
import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.Pedido.Repository.PedidoSpecification;
import projeto_base_de_telas_e_login.Pedido.dto.PedidoFiltro;
import projeto_base_de_telas_e_login.Pedido.dto.ListaDePedidoDTO;
import projeto_base_de_telas_e_login.Estoque.Estoque;
import projeto_base_de_telas_e_login.Estoque.Repository.EstoqueRepository;

import projeto_base_de_telas_e_login.Loja.Loja.Loja;
import projeto_base_de_telas_e_login.Loja.Loja.LojaRepository;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.MovimentacaoEstoque;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;
    private final LojaBairroRepository lojaBairroRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public PedidoService(PedidoRepository pedidoRepository, LojaRepository lojaRepository, EstoqueRepository estoqueRepository, LojaBairroRepository lojaBairroRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {

        this.pedidoRepository = pedidoRepository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
        this.lojaBairroRepository = lojaBairroRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @Transactional
    public void criarPedido(PedidoAddDTO dto) {

        Loja loja = lojaRepository.findById(dto.lojaId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        List<Estoque> estoquesDaLoja =
                estoqueRepository.findByLoja_Id(loja.getId());

        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new RuntimeException("Pedido precisa ter itens");
        }

        if (dto.tipoEntrega() == null) {
            throw new RuntimeException("Tipo de entrega obrigatório");
        }

        if (dto.formaDePagamento() == null) {
            throw new RuntimeException("Forma de pagamento obrigatória");
        }

        for (var itemDto : dto.itens()) {

            if (itemDto.quantidade() == null || itemDto.quantidade() <= 0) {
                throw new RuntimeException("Quantidade inválida");
            }

            Estoque estoque = estoquesDaLoja.stream()
                    .filter(e -> e.getProdutoVariacao()
                            .getId()
                            .equals(itemDto.variacaoId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Variação não encontrada no estoque"));

            if (estoque.getQuantidade() < itemDto.quantidade()) {
                throw new RuntimeException(
                        "Estoque insuficiente para: "
                                + estoque.getProdutoVariacao().getProduto().getName()
                                + " - "
                                + estoque.getProdutoVariacao().getNomeVariacao()
                );
            }
        }

        Pedido pedido = dto.toEntity(loja, estoquesDaLoja);

        pedido.calcularSubtotalProdutos();

        switch (dto.tipoEntrega()) {

            case RETIRADA -> {

                if (!loja.aceitaRetirada()) {
                    throw new RuntimeException("Esta loja não aceita retirada");
                }

                pedido.setEndereco(null);
                pedido.setBairro(null);
                pedido.setComplemento(null);
                pedido.setValorFrete(BigDecimal.ZERO);
                pedido.setFreteGratis(false);
            }

            case ENTREGA -> {

                if (!loja.aceitaEntrega()) {
                    throw new RuntimeException("Esta loja não realiza entrega");
                }

                if (dto.endereco() == null || dto.endereco().isBlank()) {
                    throw new RuntimeException("Endereço obrigatório");
                }

                LojaBairro lojaBairro =
                        lojaBairroRepository
                                .findByLoja_IdAndBairro_Id(
                                        loja.getId(),
                                        dto.bairroId()
                                )
                                .orElseThrow(() -> new RuntimeException("Bairro não atendido"));

                pedido.setEndereco(dto.endereco());
                pedido.setComplemento(dto.complemento());
                pedido.setBairro(lojaBairro.getBairro().getNome());

                BigDecimal subtotalProdutos = pedido.getTotalProdutos();
                BigDecimal minimoFreteGratis = loja.getValorMinimoFreteGratis();

                boolean freteGratis =
                        minimoFreteGratis != null
                                && subtotalProdutos.compareTo(minimoFreteGratis) >= 0;

                if (freteGratis) {
                    pedido.setValorFrete(BigDecimal.ZERO);
                    pedido.setFreteGratis(true);
                } else {
                    pedido.setValorFrete(lojaBairro.getValorFrete());
                    pedido.setFreteGratis(false);
                }
            }
        }

        pedido.calcularTotalFinal();

        for (var item : pedido.getItens()) {

            Estoque estoque = estoquesDaLoja.stream()
                    .filter(e -> e.getProdutoVariacao()
                            .getId()
                            .equals(item.getVariacaoId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado para baixa"));

            Integer quantidadeAntes = estoque.getQuantidade();
            BigDecimal precoAntes = estoque.getPrecoVenda();
            BigDecimal descontoAntes = estoque.getPercentualDesconto();

            estoque.baixarEstoque(item.getQuantidade());

            estoqueRepository.save(estoque);

            movimentacaoEstoqueRepository.save(
                    new MovimentacaoEstoque(
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
                    )
            );
        }

        pedidoRepository.save(pedido);
    }


    public Page<ListaDePedidoDTO> listarComFiltro(PedidoFiltro filtro, Pageable pageable) {
        return pedidoRepository.findAll(PedidoSpecification.filtrar(filtro), pageable).map(ListaDePedidoDTO::fromEntity);
    }

    public List<Pedido> listarPedidosDoDia() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicio = hoje.atStartOfDay();

        LocalDateTime fim = hoje.atTime(23, 59, 59);

        return pedidoRepository.findByCriadoEmBetweenOrderByCriadoEmDesc(inicio, fim);
    }

    @Transactional
    public void atualizarStatusPedido(Long id, StatusDoPedido status) {
        pedidoRepository.atualizarStatus(id, status);
    }


    public byte[] imprimirPDF(Long id) {

     //   Pedido pedido = buscarPorId(id);

        return new byte[0];
    }
}