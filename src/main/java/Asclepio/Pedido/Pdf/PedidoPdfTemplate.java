package Asclepio.Pedido.Pdf;

import Asclepio.ItemPedido.ItemPedido;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Pedido;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * HTML do comprovante de pedido, renderizado pelo openhtmltopdf.
 * O openhtmltopdf só entende CSS 2.1 (sem flex/grid), por isso o layout usa tabelas.
 * O HTML é lido como XML: todo texto vindo do usuário passa por {@link #escapar(String)}.
 * Cores seguem o verde do painel (--ds-primaria #008E51).
 */
public class PedidoPdfTemplate {

    public static String gerarHtml(Pedido pedido) {

        NumberFormat moedaBR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        String nomeEmpresa = pedido.getEmpresa() != null
                ? textoOuTraco(pedido.getEmpresa().getNome())
                : "Empresa";

        String nomeLoja = pedido.getLoja() != null
                ? textoOuTraco(pedido.getLoja().getNomeLoja())
                : "-";

        StringBuilder itensHtml = new StringBuilder();
        int linha = 0;

        for (ItemPedido item : pedido.getItens()) {

            String variacao = item.getVariacao() == null || item.getVariacao().isBlank()
                    ? ""
                    : "<div class=\"item-variacao\">" + escapar(item.getVariacao()) + "</div>";

            BigDecimal desconto = valorSeguro(item.getPercentualDesconto());
            String descontoHtml = desconto.compareTo(BigDecimal.ZERO) > 0
                    ? "<div class=\"item-desconto\">-" + desconto.stripTrailingZeros().toPlainString() + "%</div>"
                    : "";

            itensHtml.append("""
                    <tr class="%s">
                        <td><div class="item-nome">%s</div>%s</td>
                        <td class="centro">%d</td>
                        <td class="direita">%s%s</td>
                        <td class="direita forte">%s</td>
                    </tr>
                    """.formatted(
                    linha++ % 2 == 0 ? "" : "zebra",
                    textoOuTraco(item.getNomeProduto()),
                    variacao,
                    item.getQuantidade(),
                    moedaBR.format(valorSeguro(item.getPrecoUnitario())),
                    descontoHtml,
                    moedaBR.format(valorSeguro(item.getSubtotal()))
            ));
        }

        String frete = Boolean.TRUE.equals(pedido.getFreteGratis())
                ? "<span class=\"gratis\">Grátis</span>"
                : moedaBR.format(valorSeguro(pedido.getValorFrete()));

        String observacaoHtml = pedido.getObservacao() == null || pedido.getObservacao().isBlank()
                ? ""
                : """
                  <div class="secao-titulo">Observação</div>
                  <div class="observacao">%s</div>
                  """.formatted(escapar(pedido.getObservacao()));

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                        @page {
                            size: A4;
                            margin: 16mm 14mm 18mm 14mm;
                        }

                        body {
                            font-family: Helvetica, Arial, sans-serif;
                            color: #05160D;
                            font-size: 11px;
                            margin: 0;
                        }

                        table {
                            width: 100%%;
                            border-collapse: collapse;
                        }

                        /* ===== Cabeçalho ===== */
                        .topo td {
                            vertical-align: middle;
                        }

                        .empresa {
                            font-size: 22px;
                            font-weight: bold;
                            color: #006B3E;
                        }

                        .documento {
                            margin-top: 3px;
                            font-size: 11px;
                            color: #5C6862;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                        }

                        .numero-pedido {
                            background: #008E51;
                            color: #FFFFFF;
                            border-radius: 10px;
                            padding: 10px 16px;
                            text-align: right;
                        }

                        .numero-pedido .rotulo {
                            font-size: 9px;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            color: #E3F6ED;
                        }

                        .numero-pedido .valor {
                            font-size: 20px;
                            font-weight: bold;
                        }

                        .faixa {
                            height: 4px;
                            background: #008E51;
                            margin: 14px 0 16px 0;
                        }

                        /* ===== Resumo (data / status / pagamento / atendimento) ===== */
                        .resumo td {
                            width: 25%%;
                            padding: 10px 12px;
                            background: #F5FAF7;
                            border: 1px solid #E3F6ED;
                            vertical-align: top;
                        }

                        .rotulo-campo {
                            font-size: 8.5px;
                            color: #8A948F;
                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                            margin-bottom: 4px;
                        }

                        .valor-campo {
                            font-size: 11.5px;
                            font-weight: bold;
                        }

                        .status {
                            display: inline-block;
                            padding: 2px 9px;
                            border-radius: 9px;
                            font-size: 10px;
                            font-weight: bold;
                        }

                        .status-andamento { background: #E3F6ED; color: #006B3E; }
                        .status-concluido { background: #008E51; color: #FFFFFF; }
                        .status-cancelado { background: #F1F1EF; color: #5C6862; }

                        /* ===== Seções ===== */
                        .secao-titulo {
                            margin: 20px 0 8px 0;
                            font-size: 10px;
                            font-weight: bold;
                            color: #006B3E;
                            text-transform: uppercase;
                            letter-spacing: 1.2px;
                        }

                        .cartoes > tbody > tr > td {
                            width: 50%%;
                            vertical-align: top;
                        }

                        .cartao {
                            border: 1px solid #ECE8DF;
                            border-radius: 10px;
                            padding: 12px 14px;
                        }

                        .cartao table td {
                            padding: 3px 0;
                            font-size: 11px;
                            vertical-align: top;
                        }

                        .cartao .chave {
                            width: 34%%;
                            color: #5C6862;
                        }

                        /* ===== Itens ===== */
                        .itens th {
                            background: #E3F6ED;
                            color: #006B3E;
                            font-size: 9px;
                            text-transform: uppercase;
                            letter-spacing: 0.8px;
                            padding: 8px 10px;
                            text-align: left;
                            border-bottom: 2px solid #008E51;
                        }

                        .itens td {
                            padding: 9px 10px;
                            border-bottom: 1px solid #ECE8DF;
                            vertical-align: top;
                        }

                        .itens tr.zebra td {
                            background: #FAF8F3;
                        }

                        .item-nome {
                            font-weight: bold;
                        }

                        .item-variacao {
                            margin-top: 2px;
                            font-size: 9.5px;
                            color: #5C6862;
                        }

                        .item-desconto {
                            margin-top: 2px;
                            font-size: 9px;
                            font-weight: bold;
                            color: #008E51;
                        }

                        .centro, .itens th.centro { text-align: center; }
                        .direita, .itens th.direita { text-align: right; }
                        .forte { font-weight: bold; }

                        /* ===== Totais ===== */
                        .totais {
                            width: 46%%;
                            margin: 14px 0 0 auto;
                        }

                        .totais td {
                            padding: 6px 12px;
                            font-size: 11.5px;
                        }

                        .totais .linha-total td {
                            background: #008E51;
                            color: #FFFFFF;
                            font-size: 14px;
                            font-weight: bold;
                            padding: 10px 12px;
                        }

                        .gratis {
                            color: #008E51;
                            font-weight: bold;
                        }

                        .observacao {
                            border-left: 3px solid #86D8B1;
                            background: #FAF8F3;
                            padding: 10px 14px;
                            font-size: 11px;
                            line-height: 1.5;
                        }

                        /* ===== Rodapé ===== */
                        .rodape {
                            margin-top: 30px;
                            padding-top: 10px;
                            border-top: 1px solid #ECE8DF;
                            font-size: 9px;
                            color: #8A948F;
                        }

                        .rastreio {
                            font-family: Courier, monospace;
                            font-size: 10px;
                            color: #05160D;
                        }
                    </style>
                </head>
                <body>

                <table class="topo">
                    <tr>
                        <td>
                            <div class="empresa">%s</div>
                            <div class="documento">Comprovante de pedido · %s</div>
                        </td>
                        <td style="width: 150px;">
                            <div class="numero-pedido">
                                <div class="rotulo">Pedido</div>
                                <div class="valor">#%s</div>
                            </div>
                        </td>
                    </tr>
                </table>

                <div class="faixa"></div>

                <table class="resumo">
                    <tr>
                        <td>
                            <div class="rotulo-campo">Data</div>
                            <div class="valor-campo">%s</div>
                        </td>
                        <td>
                            <div class="rotulo-campo">Status</div>
                            <span class="status %s">%s</span>
                        </td>
                        <td>
                            <div class="rotulo-campo">Pagamento</div>
                            <div class="valor-campo">%s</div>
                        </td>
                        <td>
                            <div class="rotulo-campo">Atendimento</div>
                            <div class="valor-campo">%s</div>
                        </td>
                    </tr>
                </table>

                <table class="cartoes">
                    <tr>
                        <td style="padding-right: 8px;">
                            <div class="secao-titulo">Cliente</div>
                            <div class="cartao">
                                <table>
                                    <tr><td class="chave">Nome</td><td>%s</td></tr>
                                    <tr><td class="chave">E-mail</td><td>%s</td></tr>
                                    <tr><td class="chave">Telefone</td><td>%s</td></tr>
                                </table>
                            </div>
                        </td>
                        <td style="padding-left: 8px;">
                            <div class="secao-titulo">Endereço</div>
                            <div class="cartao">
                                <table>
                                    <tr><td class="chave">Endereço</td><td>%s</td></tr>
                                    <tr><td class="chave">Bairro</td><td>%s</td></tr>
                                    <tr><td class="chave">Complemento</td><td>%s</td></tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>

                <div class="secao-titulo">Itens do pedido</div>

                <table class="itens">
                    <tr>
                        <th>Produto</th>
                        <th class="centro" style="width: 50px;">Qtd</th>
                        <th class="direita" style="width: 100px;">Preço unit.</th>
                        <th class="direita" style="width: 100px;">Subtotal</th>
                    </tr>
                    %s
                </table>

                <table class="totais">
                    <tr>
                        <td>Produtos</td>
                        <td class="direita">%s</td>
                    </tr>
                    <tr>
                        <td>Frete</td>
                        <td class="direita">%s</td>
                    </tr>
                    <tr class="linha-total">
                        <td>Total</td>
                        <td class="direita">%s</td>
                    </tr>
                </table>

                %s

                <div class="rodape">
                    <table>
                        <tr>
                            <td>Código de rastreio: <span class="rastreio">%s</span></td>
                            <td class="direita">%s © %d · Documento gerado automaticamente</td>
                        </tr>
                    </table>
                </div>

                </body>
                </html>
                """.formatted(
                nomeEmpresa,
                nomeLoja,
                pedido.getId(),

                pedido.getCriadoEm().format(fmt),
                classeStatus(pedido.getStatus()),
                rotuloStatus(pedido.getStatus()),
                pedido.getFormaDePagamento() != null ? escapar(pedido.getFormaDePagamento().getDescricao()) : "-",
                rotuloAtendimento(pedido),

                textoOuTraco(pedido.getNomeCliente()),
                textoOuTraco(pedido.getEmail()),
                textoOuTraco(pedido.getTelefone()),
                textoOuTraco(pedido.getEndereco()),
                textoOuTraco(pedido.getBairro()),
                textoOuTraco(pedido.getComplemento()),

                itensHtml,

                moedaBR.format(valorSeguro(pedido.getTotalProdutos())),
                frete,
                moedaBR.format(valorSeguro(pedido.getTotalFinal())),

                observacaoHtml,

                textoOuTraco(pedido.getCodigoRastreio()),
                nomeEmpresa,
                Year.now().getValue()
        );
    }

    private static String rotuloStatus(StatusDoPedido status) {
        if (status == null) {
            return "-";
        }
        return switch (status) {
            case AGUARDANDO -> "Aguardando";
            case SEPARACAO -> "Em separação";
            case EM_TRANSITO -> "Em trânsito";
            case CONCLUIDO -> "Concluído";
            case CANCELADO -> "Cancelado";
        };
    }

    private static String classeStatus(StatusDoPedido status) {
        if (status == StatusDoPedido.CONCLUIDO) {
            return "status-concluido";
        }
        if (status == StatusDoPedido.CANCELADO) {
            return "status-cancelado";
        }
        return "status-andamento";
    }

    private static String rotuloAtendimento(Pedido pedido) {
        if (pedido.getTipoAtendimentoPedido() == null) {
            return "-";
        }
        return switch (pedido.getTipoAtendimentoPedido()) {
            case BALCAO -> "Balcão";
            case RETIRADA_NA_LOJA -> "Retirada na loja";
            case ENTREGA_PROPRIA -> "Entrega";
            case ENTREGA_TERCEIRIZADA -> "Entrega terceirizada";
        };
    }

    /** Texto do usuário pronto para o HTML (escapado), ou "-" se vazio. */
    private static String textoOuTraco(String valor) {
        return valor == null || valor.isBlank() ? "-" : escapar(valor);
    }

    // O openhtmltopdf lê o HTML como XML: um "&" ou "<" num nome quebrava a geração do PDF.
    private static String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static BigDecimal valorSeguro(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}
