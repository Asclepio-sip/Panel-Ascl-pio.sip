package projeto_base_de_telas_e_login.Pedido.dto;

import projeto_base_de_telas_e_login.ItemPedido.DTO.ItemPedidoAddDTO;
import projeto_base_de_telas_e_login.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.Estoque.Estoque;
import projeto_base_de_telas_e_login.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.Loja.Loja.Loja;
import projeto_base_de_telas_e_login.Pedido.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoAddDTO(

        Long lojaId,
        Long bairroId,

        String nomeCliente,
        String email,
        String telefone,
        String endereco,
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

        pedido.setStatus(StatusDoPedido.AGUARDANDO);
        pedido.setCriadoEm(LocalDateTime.now());

        pedido.setEndereco(endereco);
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

                    item.setProdutoId(
                            estoque.getProduto().getId()
                    );

                    item.setNomeProduto(
                            estoque.getProduto().getName()
                    );

                    item.setVariacao(
                            estoque.getProduto().getVariacao()
                    );

                    item.setImagemUrl(
                            estoque.getProduto().getImagemBase64()
                    );

                    item.setCategoria(
                            estoque.getProduto()
                                    .getCategoria()
                                    .getNomeCategoria()
                    );

                    item.setPrecoUnitario(
                            estoque.getPrecoVenda()
                    );

                    item.setQuantidade(
                            itemDto.quantidade()
                    );

                    return item;
                })
                .toList();

        pedido.setItens(itensEntity);

        if (tipoEntrega == TipoEntrega.RETIRADA) {

            if (!loja.aceitaRetirada()) {
                throw new RuntimeException(
                        "Essa loja não aceita retirada"
                );
            }

            pedido.setEndereco(null);
            pedido.setBairro(null);
            pedido.setComplemento(null);

            pedido.setValorFrete(BigDecimal.ZERO);
            pedido.setFreteGratis(false);
        }


        if (tipoEntrega == TipoEntrega.ENTREGA) {

            if (!loja.aceitaEntrega()) {
                throw new RuntimeException(
                        "Essa loja não faz entrega"
                );
            }

            if (bairroId == null) {
                throw new RuntimeException(
                        "Bairro obrigatório"
                );
            }

            if (endereco == null || endereco.isBlank()) {
                throw new RuntimeException(
                        "Endereço obrigatório"
                );
            }
        }
        return pedido;
    }
}