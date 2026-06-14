package projeto_base_de_telas_e_login.Pedido.dto;

import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;

public record PedidoCriadoResponseDTO(

        Long id,

        String codigoRastreio,

        StatusDoPedido status,

        String pdfUrl

) {
}