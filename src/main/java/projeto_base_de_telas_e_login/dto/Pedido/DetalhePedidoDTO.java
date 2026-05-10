package projeto_base_de_telas_e_login.dto.Pedido;

import projeto_base_de_telas_e_login.persistence.Pedido.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DetalhePedidoDTO(

        Long id,
        String nomeCliente,
        String email,
        String telefone,
        String endereco,
        String bairro,
        String complemento,
        String observacao,
        String status,
        String tipoEntrega,
        LocalDateTime criadoEm,
        BigDecimal totalProdutos,
        BigDecimal valorFrete,
        BigDecimal totalFinal,
        Boolean freteGratis,
        List<ItemDTO> itens

) {

    public static DetalhePedidoDTO fromEntity(Pedido pedido) {

        List<ItemDTO> itensDTO = pedido.getItens()
                .stream()
                .map(item -> new ItemDTO(
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                ))
                .toList();

        return new DetalhePedidoDTO(
                pedido.getId(),
                pedido.getNomeCliente(),
                pedido.getEmail(),
                pedido.getTelefone(),
                pedido.getEndereco(),
                pedido.getBairro(),
                pedido.getComplemento(),
                pedido.getObservacao(),
                pedido.getStatus().name(),
                pedido.getTipoEntrega().name(),
                pedido.getCriadoEm(),
                pedido.getTotalProdutos(),
                pedido.getValorFrete(),
                pedido.getTotalFinal(),
                pedido.getFreteGratis(),
                itensDTO
        );
    }

    public static class ItemDTO {

        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;

        public ItemDTO(
                String nomeProduto,
                Integer quantidade,
                BigDecimal precoUnitario,
                BigDecimal subtotal
        ) {
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.subtotal = subtotal;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public BigDecimal getPrecoUnitario() {
            return precoUnitario;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }
    }
}