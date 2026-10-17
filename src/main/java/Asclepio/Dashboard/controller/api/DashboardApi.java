package Asclepio.Dashboard.controller.api;

import Asclepio.Dashboard.dto.DashboardFinanceiroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Dashboard", description = "Indicadores financeiros da empresa")
@RequestMapping("/dashboard")
public interface DashboardApi {

    @Operation(summary = "Lucro e gastos no período", description = """
            Faturamento, custo (gastos) e lucro dos pedidos da empresa no período, com uma série
            para o gráfico e o resumo do período anterior (mesmo número de dias) para comparação.

            Regras:
            - Pedidos cancelados não entram.
            - Faturamento = soma dos itens vendidos, já com desconto, sem o frete.
            - Custo = custo guardado no item na hora da venda. Pedidos antigos (de antes do custo
              ser guardado) usam o custo atual do estoque (ver custoEstimado).
            - Item sem custo em lugar nenhum conta custo 0 (ver vendasSemCusto).
            - Período de no máximo 1 ano. Série por DIA (até 31 dias), SEMANA (até 120) ou MES.

            Exemplos:
            /dashboard/financeiro?inicio=2026-09-12&fim=2026-10-12
            /dashboard/financeiro?inicio=2026-10-06&fim=2026-10-12&lojaId=2
            """)
    @GetMapping("/financeiro")
    @PreAuthorize("hasAuthority('VerDashboard')")
    ResponseEntity<DashboardFinanceiroResponse> financeiro(
            @Parameter(description = "Data inicial (yyyy-MM-dd), inclusiva")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @Parameter(description = "Data final (yyyy-MM-dd), inclusiva")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,

            @Parameter(description = "Opcional: só os pedidos desta loja (vazio = todas as lojas da empresa)")
            @RequestParam(required = false) Long lojaId
    );
}
