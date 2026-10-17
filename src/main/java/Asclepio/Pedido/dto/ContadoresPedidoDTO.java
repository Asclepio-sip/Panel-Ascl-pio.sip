package Asclepio.Pedido.dto;

/** Quantidade de pedidos por status, com os mesmos filtros da listagem (menos o status). */
public record ContadoresPedidoDTO(
        long todos,
        long aguardando,
        long separacao,
        long emTransito,
        long concluido,
        long cancelado
) {
}
