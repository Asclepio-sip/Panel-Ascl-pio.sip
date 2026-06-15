package Asclepio.Pedido;

import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Pdf.PedidoPdfService;
import Asclepio.Pedido.Repository.PedidoRepository;
import Asclepio.Pedido.Service.*;
import Asclepio.Pedido.dto.PedidoAddDTO;
import Asclepio.Pedido.dto.PedidoCriadoResponseDTO;
import Asclepio.Pedido.dto.PedidoStatusResponseDTO;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;
    private final PedidoPdfService pedidoPdfService;

    private final PedidoValidator validator;
    private final PedidoEntregaService entregaService;
    private final PedidoEstoqueService estoqueService;
    private final PedidoCodigoService codigoService;
    private final PedidoQueryService queryService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            LojaRepository lojaRepository,
            EstoqueRepository estoqueRepository,
            PedidoPdfService pedidoPdfService,
            PedidoValidator validator,
            PedidoEntregaService entregaService,
            PedidoEstoqueService estoqueService,
            PedidoCodigoService codigoService,
            PedidoQueryService queryService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
        this.pedidoPdfService = pedidoPdfService;
        this.validator = validator;
        this.entregaService = entregaService;
        this.estoqueService = estoqueService;
        this.codigoService = codigoService;
        this.queryService = queryService;
    }

    @Transactional
    public PedidoCriadoResponseDTO criarPedido(PedidoAddDTO dto) {

        validator.validarCriacao(dto);

        Loja loja = lojaRepository.findById(dto.lojaId())
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        List<Estoque> estoquesDaLoja = estoqueRepository.findByLoja_Id(loja.getId());

        validator.validarEstoqueDosItens(dto, estoquesDaLoja);

        Pedido pedido = dto.toEntity(loja, estoquesDaLoja);

        pedido.calcularSubtotalProdutos();

        entregaService.aplicarEntrega(pedido, dto, loja);

        estoqueService.baixarEstoqueDoPedido(pedido, estoquesDaLoja);

        pedido.setCodigoRastreio(codigoService.gerarCodigoRastreioUnico());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return new PedidoCriadoResponseDTO(
                pedidoSalvo.getId(),
                pedidoSalvo.getCodigoRastreio(),
                pedidoSalvo.getStatus(),
                "/pedidos/" + pedidoSalvo.getId() + "/pdf"
        );
    }

    @Transactional
    public void atualizarStatusPedido(Long id, StatusDoPedido status) {

        if (status == null) {
            throw new BusinessException("Status do pedido é obrigatório");
        }

        Pedido pedido = queryService.buscarPorId(id);

        pedido.setStatus(status);

        if (status == StatusDoPedido.CONCLUIDO) {
            pedido.setConcluidoEm(LocalDateTime.now());
        }

        pedidoRepository.save(pedido);
    }

    public byte[] imprimirPDF(Long id) {

        Pedido pedido = queryService.buscarPorId(id);

        return pedidoPdfService.gerarPdf(pedido);
    }

    public PedidoStatusResponseDTO consultarStatusPorCodigo(String codigoRastreio) {

        Pedido pedido = queryService.buscarPorCodigoRastreio(codigoRastreio);

        if (pedido.getStatus() == StatusDoPedido.CONCLUIDO) {

            if (pedido.getConcluidoEm() == null) {
                throw new BusinessException("Data de conclusão do pedido não encontrada");
            }

            LocalDateTime limite = pedido.getConcluidoEm().plusHours(24);

            if (LocalDateTime.now().isAfter(limite)) {
                throw new BusinessException("O prazo para acompanhamento deste pedido foi encerrado");
            }
        }

        return new PedidoStatusResponseDTO(
                pedido.getCodigoRastreio(),
                pedido.getStatus()
        );
    }
}