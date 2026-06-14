package projeto_base_de_telas_e_login.Pedido.Pdf;

import projeto_base_de_telas_e_login.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.Pedido.Pedido;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PedidoPdfTemplate {

    public static String gerarHtml(Pedido pedido) {

        NumberFormat moedaBR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        String email = valorOuTraco(pedido.getEmail());
        String telefone = valorOuTraco(pedido.getTelefone());
        String endereco = valorOuTraco(pedido.getEndereco());
        String bairro = valorOuTraco(pedido.getBairro());
        String complemento = valorOuTraco(pedido.getComplemento());
        String observacao = valorOuTraco(pedido.getObservacao());

        StringBuilder itensHtml = new StringBuilder();

        for (ItemPedido item : pedido.getItens()) {
            itensHtml.append("""
                    <tr>
                        <td>%s</td>
                        <td style="text-align:center;">%d</td>
                        <td style="text-align:right;">%s</td>
                        <td style="text-align:right;">%s</td>
                    </tr>
                    """.formatted(
                    valorOuTraco(item.getNomeProduto()),
                    item.getQuantidade(),
                    moedaBR.format(valorSeguro(item.getPrecoUnitario())),
                    moedaBR.format(valorSeguro(item.getSubtotal()))
            ));
        }

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            color: #333;
                            padding: 30px;
                        }

                        .header {
                            border-bottom: 4px solid #dc2626;
                            padding-bottom: 12px;
                            margin-bottom: 25px;
                        }

                        .logo {
                            font-size: 26px;
                            font-weight: bold;
                            color: #dc2626;
                        }

                        .subtitle {
                            font-size: 13px;
                            color: #666;
                        }

                        h2 {
                            font-size: 17px;
                            color: #dc2626;
                            border-left: 5px solid #dc2626;
                            padding-left: 8px;
                            margin-top: 25px;
                        }

                        .box {
                            background: #f8f8f8;
                            padding: 14px;
                            border-radius: 8px;
                            margin-bottom: 15px;
                            font-size: 13px;
                            line-height: 1.7;
                        }

                        table {
                            width: 100%%;
                            border-collapse: collapse;
                            margin-top: 10px;
                        }

                        th {
                            background: #dc2626;
                            color: white;
                            padding: 9px;
                            font-size: 12px;
                        }

                        td {
                            padding: 8px;
                            border-bottom: 1px solid #ddd;
                            font-size: 12px;
                        }

                        .totais {
                            margin-top: 20px;
                            width: 40%%;
                            float: right;
                        }

                        .totais td {
                            border: none;
                        }

                        .total-final {
                            font-weight: bold;
                            color: #dc2626;
                            font-size: 15px;
                        }

                        .footer {
                            margin-top: 60px;
                            border-top: 1px solid #ddd;
                            padding-top: 10px;
                            text-align: center;
                            font-size: 11px;
                            color: #777;
                        }
                    </style>
                </head>
                <body>

                <div class="header">
                    <div class="logo">PROMO FARMA</div>
                    <div class="subtitle">Comprovante de Pedido</div>
                </div>

                <div class="box">
                    <strong>Pedido:</strong> #%s <br/>
                    <strong>Código de rastreio:</strong> %s <br/>
                    <strong>Data:</strong> %s <br/>
                    <strong>Status:</strong> %s <br/>
                    <strong>Pagamento:</strong> %s <br/>
                    <strong>Entrega:</strong> %s
                </div>

                <h2>Cliente</h2>
                <div class="box">
                    <strong>Nome:</strong> %s <br/>
                    <strong>Email:</strong> %s <br/>
                    <strong>Telefone:</strong> %s <br/>
                    <strong>Endereço:</strong> %s <br/>
                    <strong>Bairro:</strong> %s <br/>
                    <strong>Complemento:</strong> %s
                </div>

                <h2>Itens do Pedido</h2>

                <table>
                    <tr>
                        <th>Produto</th>
                        <th>Qtd</th>
                        <th>Preço Unitário</th>
                        <th>Subtotal</th>
                    </tr>
                    %s
                </table>

                <table class="totais">
                    <tr>
                        <td><strong>Produtos:</strong></td>
                        <td style="text-align:right;">%s</td>
                    </tr>
                    <tr>
                        <td><strong>Frete:</strong></td>
                        <td style="text-align:right;">%s</td>
                    </tr>
                    <tr class="total-final">
                        <td>Total:</td>
                        <td style="text-align:right;">%s</td>
                    </tr>
                </table>

                <div style="clear:both;"></div>

                <h2>Observação</h2>
                <div class="box">%s</div>

                <div class="footer">
                    Promo Farma © 2026 - Documento gerado automaticamente.
                </div>

                </body>
                </html>
                """.formatted(
                pedido.getId(),
                valorOuTraco(pedido.getCodigoRastreio()),
                pedido.getCriadoEm().format(fmt),
                pedido.getStatus(),
                pedido.getFormaDePagamento(),
                pedido.getTipoEntrega(),

                pedido.getNomeCliente(),
                email,
                telefone,
                endereco,
                bairro,
                complemento,

                itensHtml,

                moedaBR.format(valorSeguro(pedido.getTotalProdutos())),
                moedaBR.format(valorSeguro(pedido.getValorFrete())),
                moedaBR.format(valorSeguro(pedido.getTotalFinal())),

                observacao
        );
    }

    private static String valorOuTraco(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }

    private static BigDecimal valorSeguro(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}