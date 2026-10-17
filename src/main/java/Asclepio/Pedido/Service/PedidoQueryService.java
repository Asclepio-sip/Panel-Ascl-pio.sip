package Asclepio.Pedido.Service;

import Asclepio.Empresa.EmpresaContext;
import Asclepio.Pedido.Pedido;
import Asclepio.Pedido.Repository.PedidoRepository;
import Asclepio.Pedido.Repository.PedidoSpecification;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.dto.ContadoresPedidoDTO;
import Asclepio.Pedido.dto.DetalhePedidoDTO;
import Asclepio.Pedido.dto.ListaDePedidoDTO;
import Asclepio.Pedido.dto.PedidoFiltro;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoQueryService {

    private final PedidoRepository pedidoRepository;

    private final EmpresaContext empresaContext;

    private final EntityManager entityManager;

    public PedidoQueryService(PedidoRepository pedidoRepository, EmpresaContext empresaContext, EntityManager entityManager) {
        this.pedidoRepository = pedidoRepository;
        this.empresaContext = empresaContext;
        this.entityManager = entityManager;
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

    /** Um GROUP BY status com os filtros da listagem (o status do filtro é ignorado). */
    public ContadoresPedidoDTO contar(PedidoFiltro filtro) {

        Long empresaId = empresaContext.getEmpresaId();
        PedidoFiltro semStatus = filtro == null ? null : filtro.semStatus();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Pedido> root = query.from(Pedido.class);

        Path<StatusDoPedido> status = root.get("status");
        query.multiselect(status, cb.count(root))
                .where(PedidoSpecification.filtrar(semStatus, empresaId).toPredicate(root, query, cb))
                .groupBy(status);

        Map<StatusDoPedido, Long> porStatus = new EnumMap<>(StatusDoPedido.class);
        for (Tuple linha : entityManager.createQuery(query).getResultList()) {
            porStatus.put(linha.get(0, StatusDoPedido.class), linha.get(1, Long.class));
        }

        return new ContadoresPedidoDTO(
                porStatus.values().stream().mapToLong(Long::longValue).sum(),
                porStatus.getOrDefault(StatusDoPedido.AGUARDANDO, 0L),
                porStatus.getOrDefault(StatusDoPedido.SEPARACAO, 0L),
                porStatus.getOrDefault(StatusDoPedido.EM_TRANSITO, 0L),
                porStatus.getOrDefault(StatusDoPedido.CONCLUIDO, 0L),
                porStatus.getOrDefault(StatusDoPedido.CANCELADO, 0L)
        );
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