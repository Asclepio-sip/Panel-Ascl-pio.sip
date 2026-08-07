package Asclepio.Pedido.dto.pedido;

import Asclepio.Estoque.Estoque;
import Asclepio.ItemPedido.DTO.ItemPedidoAddDTO;
import Asclepio.ItemPedido.ItemPedido;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Pedido;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PedidoBalcaoAddDTO(

        Long lojaId,
        String nomeCliente,
        String email,
        String telefone,
        UUID clienteId,
        List<ItemPedidoAddDTO> itens,

        FormaDePagamento formaDePagamento

) {

    public Pedido toEntity(
            Loja loja,
            List<Estoque> estoquesDaLoja,
            Map<Long, ProdutoVariacaoResponseDTO> variacoesPorId
    ) {

        Pedido pedido = new Pedido();

        pedido.setLoja(loja);

        pedido.setNomeCliente(nomeCliente);
        pedido.setEmail(email);
        pedido.setTelefone(telefone);
        pedido.setFormaDePagamento(formaDePagamento);

        pedido.setCriadoEm(LocalDateTime.now());

        List<ItemPedido> itensEntity = new ArrayList<>(
                itens.stream()
                        .map(itemDto -> {

                            Estoque estoque = estoquesDaLoja.stream()
                                    .filter(e -> e.getVariacaoId().equals(itemDto.variacaoId()))
                                    .findFirst()
                                    .orElseThrow(() ->
                                            new RuntimeException("Variação não encontrada no estoque")
                                    );

                            ProdutoVariacaoResponseDTO variacao =
                                    variacoesPorId.get(itemDto.variacaoId());

                            if (variacao == null) {
                                throw new RuntimeException("Dados da variação não encontrados");
                            }


                            System.out.println("Imagem: " + variacao.imagemUrl());


                            return new ItemPedido(
                                    null,
                                    variacao.id(),
                                    variacao.produtoId(),
                                    variacao.nomeProduto(),
                                    variacao.nomeVariacao(),
                                    variacao.imagemUrl(),
                                    null,
                                    estoque.getPrecoVenda(),
                                    itemDto.quantidade(),
                                    pedido,
                                    estoque.getPercentualDesconto()
                            );

                        })
                        .toList()
        );

        pedido.setItens(itensEntity);

        return pedido;
    }
}