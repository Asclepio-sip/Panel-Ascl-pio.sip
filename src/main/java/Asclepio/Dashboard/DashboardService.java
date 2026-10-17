package Asclepio.Dashboard;

import Asclepio.Dashboard.dto.DashboardFinanceiroResponse;
import Asclepio.Dashboard.dto.DashboardFinanceiroResponse.Agrupamento;
import Asclepio.Dashboard.dto.DashboardFinanceiroResponse.ClienteComprador;
import Asclepio.Dashboard.dto.DashboardFinanceiroResponse.Ponto;
import Asclepio.Dashboard.dto.DashboardFinanceiroResponse.ProdutoVendido;
import Asclepio.Dashboard.dto.DashboardFinanceiroResponse.Resumo;
import Asclepio.Dashboard.dto.ItemVendido;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Estoque.Estoque;
import Asclepio.Estoque.Repository.EstoqueRepository;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Lucro e gastos (custo dos itens vendidos) por período.
 * Conta todos os pedidos, menos os cancelados. Faturamento = itens vendidos sem o frete.
 */
@Service
public class DashboardService {

    private static final int MAX_DIAS = 366;
    private static final int TOP = 10;
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("dd/MM");
    private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("MMM/yy", PT_BR);

    private final DashboardRepository dashboardRepository;
    private final EstoqueRepository estoqueRepository;
    private final LojaRepository lojaRepository;
    private final EmpresaContext empresaContext;

    public DashboardService(
            DashboardRepository dashboardRepository,
            EstoqueRepository estoqueRepository,
            LojaRepository lojaRepository,
            EmpresaContext empresaContext
    ) {
        this.dashboardRepository = dashboardRepository;
        this.estoqueRepository = estoqueRepository;
        this.lojaRepository = lojaRepository;
        this.empresaContext = empresaContext;
    }

    @Transactional(readOnly = true)
    public DashboardFinanceiroResponse financeiro(LocalDate inicio, LocalDate fim, Long lojaId) {

        if (inicio == null || fim == null) {
            throw new BusinessException("Informe a data inicial e a data final");
        }
        if (fim.isBefore(inicio)) {
            throw new BusinessException("A data final não pode ser antes da data inicial");
        }

        long dias = ChronoUnit.DAYS.between(inicio, fim) + 1;
        if (dias > MAX_DIAS) {
            throw new BusinessException("O período pode ter no máximo 1 ano");
        }

        Long empresaId = empresaContext.getEmpresaId();

        if (lojaId != null) {
            lojaRepository.findByIdAndEmpresa_Id(lojaId, empresaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada"));
        }

        Map<String, BigDecimal> custoAtual = new HashMap<>();

        List<ItemVendido> itens = buscarItens(empresaId, lojaId, inicio, fim);
        Agrupamento agrupamento = agrupamentoPara(dias);
        TreeMap<LocalDate, Acumulador> grupos = montarGrupos(inicio, fim, agrupamento);
        Acumulador total = new Acumulador(inicio, fim);
        Map<Long, ProdutoAcumulado> produtos = new HashMap<>();
        Map<String, ClienteAcumulado> clientes = new HashMap<>();

        for (ItemVendido item : itens) {
            Custo custo = custoDoItem(item, empresaId, custoAtual);
            grupos.floorEntry(item.criadoEm().toLocalDate()).getValue().somar(item, custo);
            total.somar(item, custo);

            produtos.computeIfAbsent(item.produtoId(), id -> new ProdutoAcumulado()).somar(item);
            clientes.computeIfAbsent(chaveCliente(item), chave -> new ClienteAcumulado()).somar(item);
        }

        // Mesmo número de dias, logo antes do período
        LocalDate fimAnterior = inicio.minusDays(1);
        LocalDate inicioAnterior = fimAnterior.minusDays(dias - 1);
        Acumulador anterior = new Acumulador(inicioAnterior, fimAnterior);
        for (ItemVendido item : buscarItens(empresaId, lojaId, inicioAnterior, fimAnterior)) {
            anterior.somar(item, custoDoItem(item, empresaId, custoAtual));
        }

        List<Ponto> serie = grupos.values().stream()
                .map(grupo -> grupo.ponto(rotulo(grupo, agrupamento)))
                .toList();

        return new DashboardFinanceiroResponse(
                inicio,
                fim,
                agrupamento,
                total.resumo(),
                anterior.resumo(),
                serie,
                produtos.values().stream()
                        .sorted(Comparator.comparingLong((ProdutoAcumulado p) -> p.quantidade)
                                .thenComparing(p -> p.faturamento)
                                .reversed())
                        .limit(TOP)
                        .map(ProdutoAcumulado::produto)
                        .toList(),
                clientes.values().stream()
                        .sorted(Comparator.comparing((ClienteAcumulado c) -> c.valor)
                                .thenComparingInt(c -> c.pedidos.size())
                                .reversed())
                        .limit(TOP)
                        .map(ClienteAcumulado::cliente)
                        .toList()
        );
    }

    private List<ItemVendido> buscarItens(Long empresaId, Long lojaId, LocalDate inicio, LocalDate fim) {
        var de = inicio.atStartOfDay();
        var ate = fim.plusDays(1).atStartOfDay();
        return lojaId == null
                ? dashboardRepository.itensVendidos(empresaId, de, ate, StatusDoPedido.CANCELADO)
                : dashboardRepository.itensVendidosDaLoja(empresaId, lojaId, de, ate, StatusDoPedido.CANCELADO);
    }

    private static Agrupamento agrupamentoPara(long dias) {
        if (dias <= 31) {
            return Agrupamento.DIA;
        }
        return dias <= 120 ? Agrupamento.SEMANA : Agrupamento.MES;
    }

    /** Grupos vazios já criados, para o gráfico mostrar também os dias/semanas sem venda. */
    private static TreeMap<LocalDate, Acumulador> montarGrupos(LocalDate inicio, LocalDate fim, Agrupamento agrupamento) {
        TreeMap<LocalDate, Acumulador> grupos = new TreeMap<>();
        LocalDate atual = inicio;

        while (!atual.isAfter(fim)) {
            LocalDate fimGrupo = switch (agrupamento) {
                case DIA -> atual;
                case SEMANA -> atual.plusDays(6);
                case MES -> atual.with(TemporalAdjusters.lastDayOfMonth());
            };
            if (fimGrupo.isAfter(fim)) {
                fimGrupo = fim;
            }
            grupos.put(atual, new Acumulador(atual, fimGrupo));
            atual = fimGrupo.plusDays(1);
        }
        return grupos;
    }

    private static String rotulo(Acumulador grupo, Agrupamento agrupamento) {
        return switch (agrupamento) {
            case DIA -> grupo.inicio.format(DIA);
            case SEMANA -> grupo.inicio.format(DIA) + " a " + grupo.fim.format(DIA);
            case MES -> grupo.inicio.format(MES).replace(".", "");
        };
    }

    /**
     * Custo do item: o guardado na venda; se o pedido é de antes disso, o custo atual do estoque
     * (estimado); se nem o estoque tem custo, fica sem custo.
     */
    private Custo custoDoItem(ItemVendido item, Long empresaId, Map<String, BigDecimal> custoAtual) {
        int quantidade = item.quantidade() == null ? 0 : item.quantidade();

        if (item.custoUnitario() != null) {
            return new Custo(item.custoUnitario().multiply(BigDecimal.valueOf(quantidade)), false);
        }

        String chave = item.lojaId() + ":" + item.variacaoId();
        if (!custoAtual.containsKey(chave)) {
            carregarCustosDaLoja(item.lojaId(), empresaId, custoAtual);
            custoAtual.putIfAbsent(chave, null);
        }

        BigDecimal unitario = custoAtual.get(chave);
        return unitario == null
                ? null
                : new Custo(unitario.multiply(BigDecimal.valueOf(quantidade)), true);
    }

    private void carregarCustosDaLoja(Long lojaId, Long empresaId, Map<String, BigDecimal> custoAtual) {
        for (Estoque estoque : estoqueRepository.findByLoja_IdAndLoja_Empresa_Id(lojaId, empresaId)) {
            custoAtual.putIfAbsent(lojaId + ":" + estoque.getVariacaoId(), estoque.getPrecoCusto());
        }
    }

    /**
     * Mesmo cliente em pedidos diferentes: pelo cadastro; sem cadastro (ex.: catálogo), pelo
     * telefone, depois e-mail, depois nome.
     */
    private static String chaveCliente(ItemVendido item) {
        if (item.clienteId() != null) {
            return "id:" + item.clienteId();
        }
        String telefone = item.telefone() == null ? "" : item.telefone().replaceAll("\\D", "");
        if (telefone.length() >= 8) {
            return "tel:" + telefone;
        }
        if (item.email() != null && !item.email().isBlank()) {
            return "email:" + item.email().trim().toLowerCase(Locale.ROOT);
        }
        String nome = item.nomeCliente() == null ? "" : item.nomeCliente().trim().toLowerCase(PT_BR);
        return "nome:" + nome;
    }

    /** Custo de um item; estimado = veio do custo atual do estoque. */
    private record Custo(BigDecimal valor, boolean estimado) {
    }

    private static final class ProdutoAcumulado {

        private Long produtoId;
        private String nome;
        private LocalDateTime ultimaVenda;
        private long quantidade;
        private BigDecimal faturamento = BigDecimal.ZERO;
        private final Set<Long> pedidos = new HashSet<>();

        private void somar(ItemVendido item) {
            produtoId = item.produtoId();
            // Nome da venda mais recente (o produto pode ter sido renomeado)
            if (ultimaVenda == null || item.criadoEm().isAfter(ultimaVenda)) {
                ultimaVenda = item.criadoEm();
                nome = item.nomeProduto();
            }
            quantidade += item.quantidade() == null ? 0 : item.quantidade();
            faturamento = faturamento.add(item.subtotal() == null ? BigDecimal.ZERO : item.subtotal());
            pedidos.add(item.pedidoId());
        }

        private ProdutoVendido produto() {
            return new ProdutoVendido(produtoId, nome, quantidade, Acumulador.dinheiro(faturamento), pedidos.size());
        }
    }

    private static final class ClienteAcumulado {

        private UUID clienteId;
        private String nome;
        private String telefone;
        private LocalDateTime ultimaCompra;
        private BigDecimal valor = BigDecimal.ZERO;
        private final Set<Long> pedidos = new HashSet<>();

        private void somar(ItemVendido item) {
            clienteId = item.clienteId();
            if (ultimaCompra == null || item.criadoEm().isAfter(ultimaCompra)) {
                ultimaCompra = item.criadoEm();
                nome = item.nomeClienteCadastro() != null ? item.nomeClienteCadastro() : item.nomeCliente();
                telefone = item.telefone();
            }
            valor = valor.add(item.subtotal() == null ? BigDecimal.ZERO : item.subtotal());
            pedidos.add(item.pedidoId());
        }

        private ClienteComprador cliente() {
            return new ClienteComprador(clienteId, nome, telefone, Acumulador.dinheiro(valor), pedidos.size(),
                    ultimaCompra.toLocalDate());
        }
    }

    private static final class Acumulador {

        private final LocalDate inicio;
        private final LocalDate fim;
        private final Set<Long> pedidos = new HashSet<>();
        private BigDecimal faturamento = BigDecimal.ZERO;
        private BigDecimal custo = BigDecimal.ZERO;
        private BigDecimal vendasSemCusto = BigDecimal.ZERO;
        private BigDecimal custoEstimado = BigDecimal.ZERO;

        private Acumulador(LocalDate inicio, LocalDate fim) {
            this.inicio = inicio;
            this.fim = fim;
        }

        private void somar(ItemVendido item, Custo custoItem) {
            BigDecimal subtotal = item.subtotal() == null ? BigDecimal.ZERO : item.subtotal();
            pedidos.add(item.pedidoId());
            faturamento = faturamento.add(subtotal);

            if (custoItem == null) {
                vendasSemCusto = vendasSemCusto.add(subtotal);
                return;
            }
            custo = custo.add(custoItem.valor());
            if (custoItem.estimado()) {
                custoEstimado = custoEstimado.add(custoItem.valor());
            }
        }

        private BigDecimal lucro() {
            return faturamento.subtract(custo);
        }

        private Ponto ponto(String rotulo) {
            return new Ponto(inicio, fim, rotulo, dinheiro(faturamento), dinheiro(custo), dinheiro(lucro()), pedidos.size());
        }

        private Resumo resumo() {
            boolean semVendas = faturamento.signum() == 0;
            return new Resumo(
                    dinheiro(faturamento),
                    dinheiro(custo),
                    dinheiro(lucro()),
                    semVendas ? null : lucro().multiply(BigDecimal.valueOf(100)).divide(faturamento, 1, RoundingMode.HALF_UP),
                    pedidos.size(),
                    pedidos.isEmpty() ? null : faturamento.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP),
                    dinheiro(vendasSemCusto),
                    dinheiro(custoEstimado)
            );
        }

        private static BigDecimal dinheiro(BigDecimal valor) {
            return valor.setScale(2, RoundingMode.HALF_UP);
        }
    }
}
