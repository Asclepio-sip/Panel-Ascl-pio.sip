package Asclepio.CatalogoOnline.dto;

import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.dto.PedidoCriadoResponseDTO;

// Resposta pública: sem id interno nem link do PDF (que exige login).
public record CatalogoPedidoResponse(
        String codigoRastreio,
        StatusDoPedido status,
        String statusUrl
) {

    public static CatalogoPedidoResponse fromPedidoCriado(PedidoCriadoResponseDTO pedido) {
        return new CatalogoPedidoResponse(
                pedido.codigoRastreio(),
                pedido.status(),
                "/pedidos/status/" + pedido.codigoRastreio()
        );
    }
}
