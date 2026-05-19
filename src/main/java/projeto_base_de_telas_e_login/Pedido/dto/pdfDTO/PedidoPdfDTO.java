package projeto_base_de_telas_e_login.Pedido.dto.pdfDTO;

import java.util.List;

public record PedidoPdfDTO (
         Long id,
         String criado,
         String cliente,
         String telefone,
         String endereco,
         String bairro,
         String complemento,
         String formaPagamento,
         String total,
         List<ItemPedidoPdfDTO> itens
) {
}