package Asclepio.Dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DashboardFinanceiroResponse(

        LocalDate inicio,
        LocalDate fim,

        @Schema(description = "Tamanho de cada ponto da série: DIA (até 31 dias), SEMANA (até 120 dias) ou MES")
        Agrupamento agrupamento,

        Resumo resumo,

        @Schema(description = "Mesmo número de dias, logo antes do período (para comparação)")
        Resumo periodoAnterior,

        List<Ponto> serie,

        @Schema(description = "Até 10 produtos com mais unidades vendidas no período")
        List<ProdutoVendido> produtosMaisVendidos,

        @Schema(description = "Até 10 clientes que mais gastaram no período")
        List<ClienteComprador> clientesQueMaisCompraram

) {

    public enum Agrupamento { DIA, SEMANA, MES }

    public record Resumo(

            @Schema(description = "Soma dos itens vendidos (sem frete), já com desconto")
            BigDecimal faturamento,

            @Schema(description = "Custo dos itens vendidos (gastos)")
            BigDecimal custo,

            @Schema(description = "faturamento - custo")
            BigDecimal lucro,

            @Schema(description = "lucro / faturamento em %; null sem vendas")
            BigDecimal margem,

            long pedidos,

            @Schema(description = "faturamento / pedidos; null sem pedidos")
            BigDecimal ticketMedio,

            @Schema(description = "Faturamento de itens sem custo informado (o custo deles conta como 0)")
            BigDecimal vendasSemCusto,

            @Schema(description = "Parte do custo estimada pelo custo atual do estoque (pedidos feitos antes de o custo ser guardado na venda)")
            BigDecimal custoEstimado

    ) {
    }

    public record Ponto(
            LocalDate inicio,
            LocalDate fim,
            String rotulo,
            BigDecimal faturamento,
            BigDecimal custo,
            BigDecimal lucro,
            long pedidos
    ) {
    }

    public record ProdutoVendido(
            Long produtoId,
            String nome,
            long quantidade,
            @Schema(description = "Soma dos itens desse produto (sem frete), já com desconto")
            BigDecimal faturamento,
            long pedidos
    ) {
    }

    public record ClienteComprador(
            @Schema(description = "Cliente cadastrado; null quando o pedido não tem cliente vinculado (agrupado por telefone, e-mail ou nome)")
            UUID clienteId,
            String nome,
            String telefone,
            @Schema(description = "Soma dos itens comprados (sem frete), já com desconto")
            BigDecimal valor,
            long pedidos,
            LocalDate ultimaCompra
    ) {
    }
}
