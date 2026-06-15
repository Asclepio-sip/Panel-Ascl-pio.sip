package Asclepio.Pedido;

import Asclepio.Pedido.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import Asclepio.Loja.LojaBairro.LojaBairro;
import Asclepio.Loja.LojaBairro.LojaBairroRepository;
import Asclepio.MovimentacaoEstoque.Repository.MovimentacaoEstoqueRepository;
import Asclepio.Pedido.Pdf.PedidoPdfService;
import Asclepio.Pedido.Repository.PedidoRepository;
import projeto_base_de_telas_e_login.Pedido.dto.*;
import org.springframework.transaction.annotation.Transactional;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Repository.PedidoSpecification;
import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;

import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.LojaRepository;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoque;
import Asclepio.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import Asclepio.config.security.CodigoRastreioGenerator;

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
    private final PedidoPdfService pedidoPdfService;

    public PedidoService(PedidoRepository pedidoRepository, LojaRepository lojaRepository, EstoqueRepository estoqueRepository, LojaBairroRepository lojaBairroRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, PedidoPdfService pedidoPdfService) {
        this.pedidoRepository = pedidoRepository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
        this.lojaBairroRepository = lojaBairroRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.pedidoPdfService = pedidoPdfService;
    }

    @Transactional
    public PedidoCriadoResponseDTO criarPedido(PedidoAddDTO dto) {

        Loja loja = lojaRepository.findById(dto.lojaId()).orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        List<Estoque> estoquesDaLoja = estoqueRepository.findByLoja_Id(loja.getId());

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

            Estoque estoque = estoquesDaLoja.stream().filter(e -> e.getProdutoVariacao().getId().equals(itemDto.variacaoId())).findFirst().orElseThrow(() -> new RuntimeException("Variação não encontrada no estoque"));

            if (estoque.getQuantidade() < itemDto.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + estoque.getProdutoVariacao().getProduto().getName() + " - " + estoque.getProdutoVariacao().getNomeVariacao());
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

                LojaBairro lojaBairro = lojaBairroRepository.findByLoja_IdAndBairro_Id(loja.getId(), dto.bairroId()).orElseThrow(() -> new RuntimeException("Bairro não atendido"));

                pedido.setEndereco(dto.endereco());
                pedido.setComplemento(dto.complemento());
                pedido.setBairro(lojaBairro.getBairro().getNome());

                BigDecimal subtotalProdutos = pedido.getTotalProdutos();
                BigDecimal minimoFreteGratis = loja.getValorMinimoFreteGratis();

                boolean freteGratis = minimoFreteGratis != null && subtotalProdutos.compareTo(minimoFreteGratis) >= 0;

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

            Estoque estoque = estoquesDaLoja.stream().filter(e -> e.getProdutoVariacao().getId().equals(item.getVariacaoId())).findFirst().orElseThrow(() -> new RuntimeException("Estoque não encontrado para baixa"));

            Integer quantidadeAntes = estoque.getQuantidade();
            BigDecimal precoAntes = estoque.getPrecoVenda();
            BigDecimal descontoAntes = estoque.getPercentualDesconto();

            estoque.baixarEstoque(item.getQuantidade());

            estoqueRepository.save(estoque);

            movimentacaoEstoqueRepository.save(new MovimentacaoEstoque(estoque, estoque.getLoja(), estoque.getProdutoVariacao().getProduto(), estoque.getProdutoVariacao(), null, TipoMovimentacaoEstoque.SAIDA_PEDIDO, quantidadeAntes, estoque.getQuantidade(), precoAntes, estoque.getPrecoVenda(), descontoAntes, estoque.getPercentualDesconto(), "Baixa automática por pedido"));
        }
        pedido.setCodigoRastreio(gerarCodigoRastreioUnico());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return new PedidoCriadoResponseDTO(

                pedidoSalvo.getId(),

                pedidoSalvo.getCodigoRastreio(),

                pedidoSalvo.getStatus(),

                "/pedidos/" + pedidoSalvo.getId() + "/pdf"
        );
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

        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(status);

        if (status == StatusDoPedido.CONCLUIDO) {
            pedido.setConcluidoEm(LocalDateTime.now());
        }

        pedidoRepository.save(pedido);
    }


    public byte[] imprimirPDF(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return pedidoPdfService.gerarPdf(pedido);
    }


    private String gerarCodigoRastreioUnico() {
        String codigo;

        do {
            codigo = CodigoRastreioGenerator.gerar();
        } while (pedidoRepository.existsByCodigoRastreio(codigo));

        return codigo;
    }

    public PedidoStatusResponseDTO consultarStatusPorCodigo(String codigoRastreio) {

        Pedido pedido = pedidoRepository.findByCodigoRastreio(codigoRastreio).orElseThrow(() -> new RuntimeException("Código de rastreio inválido"));

        if (pedido.getStatus() == StatusDoPedido.CONCLUIDO) {

            if (pedido.getConcluidoEm() == null) {
                throw new RuntimeException("Data de conclusão do pedido não encontrada");
            }

            LocalDateTime limite = pedido.getConcluidoEm().plusHours(24);

            if (LocalDateTime.now().isAfter(limite)) {
                throw new RuntimeException("O prazo para acompanhamento deste pedido foi encerrado");
            }
        }

        return new PedidoStatusResponseDTO(pedido.getCodigoRastreio(), pedido.getStatus());
    }
}