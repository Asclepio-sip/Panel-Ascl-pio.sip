package projeto_base_de_telas_e_login.Pedido.dto.pdfDTO;

public record ItemPedidoPdfDTO (
        String nomeProduto,
        String imagemUrl,
        Integer quantidade,
        String precoUnitario,
        String subtotal
){
}