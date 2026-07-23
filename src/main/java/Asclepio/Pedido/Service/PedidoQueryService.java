package Asclepio.Pedido.Service;

import Asclepio.Empresa.EmpresaContext;
import Asclepio.Pedido.Pedido;
import Asclepio.Pedido.Repository.PedidoRepository;
import Asclepio.Pedido.Repository.PedidoSpecification;
import Asclepio.Pedido.dto.DetalhePedidoDTO;
import Asclepio.Pedido.dto.ListaDePedidoDTO;
import Asclepio.Pedido.dto.PedidoFiltro;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoQueryService {

    private final PedidoRepository pedidoRepository;

    private final EmpresaContext empresaContext;

    public PedidoQueryService(PedidoRepository pedidoRepository, EmpresaContext empresaContext) {
        this.pedidoRepository = pedidoRepository;
        this.empresaContext = empresaContext;
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    public Pedido buscarPorCodigoRastreio(String codigoRastreio) {
        return pedidoRepository.findByCodigoRastreio(codigoRastreio).orElseThrow(() -> new ResourceNotFoundException("Código de rastreio inválido"));
    }

    public Page<ListaDePedidoDTO> listarComFiltro(PedidoFiltro filtro, Pageable pageable) {

        Long empresaId = empresaContext.getEmpresaId();

        return pedidoRepository.findAll(PedidoSpecification.filtrar(filtro, empresaId), pageable).map(ListaDePedidoDTO::fromEntity);
    }

    public List<Pedido> listarPedidosDoDia() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.atTime(23, 59, 59);

        return pedidoRepository.findByCriadoEmBetweenOrderByCriadoEmDesc(inicio, fim);
    }

    public Pedido buscarPorIdDaEmpresa(Long id, Long empresaId) {

        if (id == null) {
            throw new BusinessException("ID do pedido é obrigatório");
        }

        return pedidoRepository.findByIdAndEmpresa_Id(id, empresaId).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }

    public DetalhePedidoDTO buscarDetalhes(Long id) {

        Long empresaId = empresaContext.getEmpresaId();
        Pedido pedido = buscarPorIdDaEmpresa(id, empresaId);
        return DetalhePedidoDTO.fromEntity(pedido);
    }
}