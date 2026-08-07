package Asclepio.Pedido;

import Asclepio.ClienteEmpresa.ClienteEmpresa;
import Asclepio.ClienteEmpresa.ClienteEmpresaRepository;
import Asclepio.ClienteEmpresa.ClienteEmpresaService;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.ItemPedido.DTO.ItemPedidoAddDTO;
import Asclepio.Loja.FormaPagamento.LojaFormaPagamentoService;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Enum.TipoAtendimentoPedido;
import Asclepio.Pedido.Pdf.PedidoPdfService;
import Asclepio.Pedido.Repository.PedidoRepository;
import Asclepio.Pedido.Service.*;
import Asclepio.Pedido.dto.pedido.PedidoAddDTO;
import Asclepio.Pedido.dto.PedidoCriadoResponseDTO;
import Asclepio.Pedido.dto.PedidoStatusResponseDTO;
import Asclepio.Pedido.dto.pedido.PedidoBalcaoAddDTO;
import Asclepio.ProdutoVariacao.ProdutoVariacaoStorageClient;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LojaRepository lojaRepository;
    private final EstoqueRepository estoqueRepository;
    private final PedidoPdfService pedidoPdfService;

    private final PedidoValidator validator;
    private final PedidoAtendimentoService atendimentoService;
    private final PedidoEstoqueService estoqueService;
    private final PedidoCodigoService codigoService;
    private final PedidoQueryService queryService;

    private final EmpresaContext empresaContext;

    private final ProdutoVariacaoStorageClient produtoVariacaoClient;

    private final ClienteEmpresaRepository clienteEmpresaRepository;

    private final LojaFormaPagamentoService formaPagamentoService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            LojaRepository lojaRepository,
            EstoqueRepository estoqueRepository,
            PedidoPdfService pedidoPdfService,
            PedidoValidator validator,
            PedidoAtendimentoService atendimentoService,
            PedidoEstoqueService estoqueService,
            PedidoCodigoService codigoService,
            PedidoQueryService queryService,
            ProdutoVariacaoStorageClient produtoVariacaoClient,
            EmpresaContext empresaContext,
            ClienteEmpresaRepository clienteEmpresaRepository,
            LojaFormaPagamentoService formaPagamentoService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.lojaRepository = lojaRepository;
        this.estoqueRepository = estoqueRepository;
        this.pedidoPdfService = pedidoPdfService;
        this.validator = validator;
        this.atendimentoService = atendimentoService;
        this.estoqueService = estoqueService;
        this.codigoService = codigoService;
        this.queryService = queryService;
        this.produtoVariacaoClient = produtoVariacaoClient;
        this.empresaContext = empresaContext;
        this.clienteEmpresaRepository =clienteEmpresaRepository;
        this.formaPagamentoService = formaPagamentoService;
    }

    @Transactional
    public PedidoCriadoResponseDTO criarPedido(PedidoAddDTO dto) {

        validator.validarCriacao(dto);

        Loja loja = lojaRepository.findById(dto.lojaId())
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));

        formaPagamentoService.validarFormaPagamento(
                loja,
                dto.formaDePagamento()
        );

        List<Estoque> estoquesDaLoja =
                estoqueRepository.findByLoja_IdAndLoja_Empresa_Id(
                        loja.getId(),
                        loja.getEmpresa().getId()
                );

        validator.validarEstoqueDosItens(dto, estoquesDaLoja);

        Map<Long, ProdutoVariacaoResponseDTO> variacoesPorId = dto.itens()
                .stream()
                .collect(Collectors.toMap(
                        ItemPedidoAddDTO::variacaoId,
                        item -> produtoVariacaoClient.buscarPorId(item.variacaoId())
                ));

        Pedido pedido = dto.toEntity(loja, estoquesDaLoja, variacoesPorId);

        pedido.setEmpresa(loja.getEmpresa());

        pedido.calcularSubtotalProdutos();

        atendimentoService.aplicarAtendimento(pedido, dto, loja);

        estoqueService.baixarEstoqueDoPedido(pedido, estoquesDaLoja);

        pedido.setCodigoRastreio(codigoService.gerarCodigoRastreioUnico());

        pedido.definirStatusInicial();

        System.out.println("Tipo: " + pedido.getTipoAtendimentoPedido());
        System.out.println("Status: " + pedido.getStatus());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return new PedidoCriadoResponseDTO(
                pedidoSalvo.getId(),
                pedidoSalvo.getCodigoRastreio(),
                pedidoSalvo.getStatus(),
                "/pedidos/" + pedidoSalvo.getId() + "/pdf"
        );
    }

    @Transactional
    public PedidoCriadoResponseDTO criarPedidoBalcao(PedidoBalcaoAddDTO dto){

        validator.validarCriacaoBalcao(dto);

        Long empresaId = empresaContext.getEmpresaId();

        Loja loja = lojaRepository
                .findByIdAndEmpresa_Id(dto.lojaId(), empresaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loja não encontrada"));

        formaPagamentoService.validarFormaPagamento(
                loja,
                dto.formaDePagamento()
        );

        List<Estoque> estoquesDaLoja =
                estoqueRepository.findByLoja_IdAndLoja_Empresa_Id(loja.getId(), empresaId);

        validator.validarEstoqueDosItensBalcao(dto, estoquesDaLoja);

        Map<Long, ProdutoVariacaoResponseDTO> variacoesPorId = dto.itens()
                .stream()
                .collect(Collectors.toMap(
                        ItemPedidoAddDTO::variacaoId,
                        item -> produtoVariacaoClient.buscarPorId(item.variacaoId())
                ));

        Pedido pedido = dto.toEntity(loja, estoquesDaLoja, variacoesPorId);

        pedido.setTipoAtendimentoPedido(TipoAtendimentoPedido.BALCAO);

        pedido.setEmpresa(loja.getEmpresa());

        pedido.calcularSubtotalProdutos();

        if (dto.clienteId() != null) {

            ClienteEmpresa cliente = clienteEmpresaRepository
                    .findByIdAndEmpresa_Id(dto.clienteId(), empresaId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Cliente não encontrado."));

            pedido.setCliente(cliente);

            // Snapshot dos dados
            pedido.setNomeCliente(cliente.getNome());
            pedido.setEmail(cliente.getEmail());
            pedido.setTelefone(cliente.getNumero());
        }

        atendimentoService.aplicarAtendimento(pedido, loja);

        estoqueService.baixarEstoqueDoPedido(pedido, estoquesDaLoja);

        pedido.setCodigoRastreio(codigoService.gerarCodigoRastreioUnico());

        pedido.definirStatusInicial();

        pedido.setStatus(StatusDoPedido.CONCLUIDO);

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

        Long empresaId = empresaContext.getEmpresaId();

        Pedido pedido = queryService.buscarPorIdDaEmpresa(id, empresaId);

        pedido.setStatus(status);

        if (status == StatusDoPedido.CONCLUIDO) {
            pedido.setConcluidoEm(LocalDateTime.now());
        }

        pedidoRepository.save(pedido);
    }

    public byte[] imprimirPDF(Long id) {

        Long empresaId = empresaContext.getEmpresaId();

        Pedido pedido = queryService.buscarPorIdDaEmpresa(id, empresaId);

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