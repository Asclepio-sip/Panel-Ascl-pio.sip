package Asclepio.Pedido.dto;

import Asclepio.Pedido.Enum.StatusDoPedido;

public record PedidoCriadoResponseDTO(

        Long id,

        String codigoRastreio,

        StatusDoPedido status,

        String pdfUrl

) {
}