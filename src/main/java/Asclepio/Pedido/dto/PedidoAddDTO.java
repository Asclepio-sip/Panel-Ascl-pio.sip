package Asclepio.Pedido.dto;

import Asclepio.Estoque.Estoque;
import Asclepio.ItemPedido.DTO.ItemPedidoAddDTO;
import Asclepio.ItemPedido.ItemPedido;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Enum.TipoEntrega;
import Asclepio.Pedido.Pedido;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            List<Estoque> estoquesDaLoja,
            Map<Long, ProdutoVariacaoResponseDTO> variacoesPorId
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

        List<ItemPedido> itensEntity = new ArrayList<>(
                itens.stream().map(itemDto -> {

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

                            return new ItemPedido(
                                    null,
                                    variacao.id(),
                                    variacao.produtoId(),
                                    variacao.nomeProduto(),
                                    variacao.nomeVariacao(),
                                    null,
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

        if (tipoEntrega == TipoEntrega.RETIRADA) {

            if (!loja.aceitaRetirada()) {
                throw new RuntimeException("Essa loja não aceita retirada");
            }

            pedido.setEndereco(null);
            pedido.setBairro(null);
            pedido.setComplemento(null);

            pedido.setValorFrete(BigDecimal.ZERO);
            pedido.setFreteGratis(false);
        }

        if (tipoEntrega == TipoEntrega.ENTREGA) {

            if (!loja.aceitaEntrega()) {
                throw new RuntimeException("Essa loja não faz entrega");
            }

            if (bairroId == null) {
                throw new RuntimeException("Bairro obrigatório");
            }

            if (endereco == null || endereco.isBlank()) {
                throw new RuntimeException("Endereço obrigatório");
            }
        }

        return pedido;
    }
}