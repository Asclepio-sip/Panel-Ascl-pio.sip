package projeto_base_de_telas_e_login.dto.Pedido;

import projeto_base_de_telas_e_login.model.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.model.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.entidade.Estoque;
import projeto_base_de_telas_e_login.entidade.ItemPedido;
import projeto_base_de_telas_e_login.entidade.Loja;
import projeto_base_de_telas_e_login.entidade.Pedido;

import java.math.BigDecimal;
import java.util.List;

public record PedidoAddDTO(

        Long lojaId,
        String nomeCliente,
        String email,
        String telefone,
        String endereco,
        String bairro,
        String complemento,
        String observacao,
        TipoEntrega tipoEntrega,
        List<ItemPedidoAddDTO> itens,
        FormaDePagamento formaDePagamento

) {

    public Pedido toEntity(
            Loja loja,
            List<Estoque> estoquesDaLoja
    ) {

        Pedido pedido = new Pedido();

        pedido.setLoja(loja);

        pedido.setNomeCliente(nomeCliente);
        pedido.setEmail(email);
        pedido.setTelefone(telefone);

        pedido.setEndereco(endereco);
        pedido.setBairro(bairro);
        pedido.setComplemento(complemento);
        pedido.setObservacao(observacao);

        pedido.setTipoEntrega(tipoEntrega);
        pedido.setFormaDePagamento(formaDePagamento);

        List<ItemPedido> itensEntity = itens.stream()
                .map(itemDto -> {

                    Estoque estoque = estoquesDaLoja.stream()
                            .filter(e ->
                                    e.getProduto().getId()
                                            .equals(itemDto.produtoId())
                            )
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Produto não encontrado no estoque"
                                    )
                            );

                    ItemPedido item = new ItemPedido();

                    item.setPedido(pedido);

                    item.setNomeProduto(
                            estoque.getProduto().getName()
                    );

                    item.setPrecoUnitario(
                            estoque.getPrecoVenda()
                    );

                    item.setQuantidade(
                            itemDto.quantidade()
                    );

                    item.setSubtotal(
                            estoque.getPrecoVenda()
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    itemDto.quantidade()
                                            )
                                    )
                    );

                    return item;
                })
                .toList();

        pedido.setItens(itensEntity);

        pedido.calcularTotais();

        return pedido;
    }
}