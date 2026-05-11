//package projeto_base_de_telas_e_login.persistence.Pedido.PDF.PdfTemplatePedido;
//
//import projeto_base_de_telas_e_login.model.ItemPedido.ItemPedido;
//
//import java.text.NumberFormat;
//import java.time.format.DateTimeFormatter;
//import java.util.Locale;
//
//public class PdfPedidoTemplate {
//
//    public static String gerarHtml(Pedido pedido) {
//
//        NumberFormat moedaBR = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
//
//        String emailCliente = pedido.getEmail() != null ? pedido.getEmail() : "-";
//        String telefoneCliente = pedido.getTelefone() != null ? pedido.getTelefone() : "-";
//        String complemento = pedido.getComplemento() != null ? pedido.getComplemento() : "-";
//        String observacao = pedido.getObservacao() != null ? pedido.getObservacao() : "-";
//
//        StringBuilder itensHtml = new StringBuilder();
//
//        for (ItemPedido item : pedido.getItens()) {
//            itensHtml.append("""
//            <tr>
//                <td>%s</td>
//                <td style="text-align:center;">%d</td>
//                <td style="text-align:right;">%s</td>
//                <td style="text-align:right;">%s</td>
//            </tr>
//        """.formatted(
//                    item.getNomeProduto(),
//                    item.getQuantidade(),
//                    moedaBR.format(item.getPrecoUnitario().getValor()),
//                    moedaBR.format(item.getSubtotal().getValor())
//            ));
//        }
//
//        return """
//<!DOCTYPE html>
//<html>
//<head>
//<meta charset="UTF-8"/>
//<title>Promo Farma - Pedido</title>
//
//<style>
//    body {
//        font-family: Arial, sans-serif;
//        background-color: #ffffff;
//        padding: 40px;
//        color: #333;
//    }
//
//    .header {
//        border-bottom: 4px solid #c40000;
//        padding-bottom: 15px;
//        margin-bottom: 30px;
//    }
//
//    .logo {
//        font-size: 28px;
//        font-weight: bold;
//        color: #c40000;
//    }
//
//    .subtitulo {
//        color: #666;
//        font-size: 14px;
//    }
//
//    h2 {
//        color: #c40000;
//        border-left: 6px solid #c40000;
//        padding-left: 10px;
//    }
//
//    table {
//        width: 100%%;
//        border-collapse: collapse;
//        margin-top: 15px;
//    }
//
//    th {
//        background-color: #c40000;
//        color: white;
//        padding: 10px;
//        font-size: 13px;
//    }
//
//    td {
//        padding: 8px;
//        border-bottom: 1px solid #ddd;
//        font-size: 13px;
//    }
//
//    .totais {
//        margin-top: 20px;
//        width: 40%%;
//        float: right;
//    }
//
//    .totais td {
//        border: none;
//        padding: 6px;
//    }
//
//    .total-final {
//        font-size: 16px;
//        font-weight: bold;
//        color: #c40000;
//    }
//
//    .box {
//        background-color: #f9f9f9;
//        padding: 15px;
//        border-radius: 8px;
//        margin-bottom: 25px;
//    }
//
//    .footer {
//        margin-top: 60px;
//        font-size: 12px;
//        color: #888;
//        text-align: center;
//        border-top: 1px solid #ddd;
//        padding-top: 10px;
//    }
//</style>
//</head>
//
//<body>
//
//<div class="header">
//    <div class="logo">PROMO FARMA</div>
//    <div class="subtitulo">Comprovante de Pedido</div>
//</div>
//
//<div class="box">
//    <strong>Pedido Nº:</strong> %s <br/>
//    <strong>Data:</strong> %s <br/>
//    <strong>Status:</strong> %s <br/>
//    <strong>Forma de Pagamento:</strong> %s <br/>
//    <strong>Tipo de Entrega:</strong> %s
//</div>
//
//<h2>Dados do Cliente</h2>
//
//<div class="box">
//    <strong>Nome:</strong> %s <br/>
//    <strong>Email:</strong> %s <br/>
//    <strong>Telefone:</strong> %s <br/>
//    <strong>Endereço:</strong> %s <br/>
//    <strong>Bairro:</strong> %s <br/>
//    <strong>Complemento:</strong> %s
//</div>
//
//<h2>Itens do Pedido</h2>
//
//<table>
//    <tr>
//        <th>Produto</th>
//        <th>Qtd</th>
//        <th>Preço Unitário</th>
//        <th>Subtotal</th>
//    </tr>
//    %s
//</table>
//
//<table class="totais">
//    <tr>
//        <td><strong>Subtotal:</strong></td>
//        <td style="text-align:right;">%s</td>
//    </tr>
//    <tr>
//        <td><strong>Frete:</strong></td>
//        <td style="text-align:right;">%s</td>
//    </tr>
//    <tr class="total-final">
//        <td>Total Final:</td>
//        <td style="text-align:right;">%s</td>
//    </tr>
//</table>
//
//<div style="clear:both;"></div>
//
//<h2>Observações</h2>
//
//<div class="box">
//    %s
//</div>
//
//<div class="footer">
//    Promo Farma © 2026 - Documento gerado automaticamente.
//</div>
//
//</body>
//</html>
//""".formatted(
//                pedido.getId(),
//                pedido.getCriadoEm().format(fmt),
//                pedido.getStatus(),
//                pedido.getFormaPagamento(),
//                pedido.getTipoEntrega(),
//
//                pedido.getNomeCliente(),
//                emailCliente,
//                telefoneCliente,
//                pedido.getEndereco(),
//                pedido.getBairro(),
//                complemento,
//
//                itensHtml.toString(),
//
//                moedaBR.format(pedido.getTotalProdutos().getValor()),
//                moedaBR.format(pedido.getValorFrete().getValor()),
//                moedaBR.format(pedido.getTotalFinal().getValor()),
//
//                observacao
//        );
//    }
//}