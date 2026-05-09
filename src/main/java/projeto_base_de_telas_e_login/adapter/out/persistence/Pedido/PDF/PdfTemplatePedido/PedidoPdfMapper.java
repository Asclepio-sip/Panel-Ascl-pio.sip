package projeto_base_de_telas_e_login.adapter.out.persistence.Pedido.PDF.PdfTemplatePedido;

import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;
import projeto_base_de_telas_e_login.domain.model.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.domain.model.Preco.Preco;

import java.math.BigDecimal;
import java.util.List;

public class PedidoPdfMapper {

    public static PedidoPdfDTO from(Pedido pedido) {

        List<ItemPedidoPdfDTO> itens = pedido.getItens()
                .stream()
                .map(PedidoPdfMapper::mapItem)
                .toList();

        return new PedidoPdfDTO(

                // =========================
                // IDENTIFICAÇÃO
                // =========================
                pedido.getId(),
                null, // numeroPedidoExterno (implementar depois se quiser)
                pedido.getCriadoEm(),
                null, // pagoEm

                pedido.getStatus() != null ? pedido.getStatus().name() : null,
                null, // statusPagamento

                // =========================
                // ENTREGA
                // =========================
                pedido.getTipoEntrega() != null ? pedido.getTipoEntrega().name() : null,
                null, // codigoRastreio
                null, // previsaoEntrega

                // =========================
                // PAGAMENTO
                // =========================
                pedido.getFormaPagamento(),
                null, // idTransacao
                BigDecimal.ZERO, // valorTroco

                // =========================
                // CLIENTE
                // =========================
                null, // clienteId
                pedido.getNomeCliente(),
                pedido.getEmail(),
                pedido.getTelefone(),

                // =========================
                // ENDEREÇO
                // =========================
                pedido.getEndereco(),
                null, // numero
                pedido.getBairro(),
                null, // cidade
                null, // cep
                pedido.getComplemento(),

                // =========================
                // OBSERVAÇÃO
                // =========================
                pedido.getObservacao(),

                // =========================
                // VALORES
                // =========================
                getValorSeguro(pedido.getTotalProdutos()),
                getValorSeguro(pedido.getValorFrete()),
                BigDecimal.ZERO, // valorDesconto
                null, // cupomDesconto
                BigDecimal.ZERO, // outrasTaxas
                getValorSeguro(pedido.getTotalFinal()),
                pedido.getFreteGratis() != null ? pedido.getFreteGratis() : false,

                // =========================
                // ITENS
                // =========================
                itens
        );
    }

    private static ItemPedidoPdfDTO mapItem(ItemPedido item) {
        return new ItemPedidoPdfDTO(
                item.getNomeProduto(),
                item.getQuantidade(),
                item.getPrecoUnitario() != null
                        ? item.getPrecoUnitario().getValor()
                        : BigDecimal.ZERO,
                item.getSubtotal() != null
                        ? item.getSubtotal().getValor()
                        : BigDecimal.ZERO
        );
    }

    private static BigDecimal getValorSeguro(Preco preco) {
        return preco != null ? preco.getValor() : BigDecimal.ZERO;
    }
}