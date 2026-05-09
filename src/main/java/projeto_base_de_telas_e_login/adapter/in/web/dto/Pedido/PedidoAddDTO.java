package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido;

import projeto_base_de_telas_e_login.adapter.out.persistence.Estoque.EstoqueEntity;
import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.domain.model.Pedido.Pedido;
import projeto_base_de_telas_e_login.domain.model.product.Product;
import projeto_base_de_telas_e_login.domain.model.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.domain.model.Preco.Preco;

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

    public Pedido toDomain(List<EstoqueEntity> estoquesDaLoja) {
        Loja lojaStub = new Loja(lojaId, null, null);

        return new Pedido(
                lojaStub,
                nomeCliente,
                email,
                telefone,
                endereco,
                bairro,
                complemento,
                observacao,
                tipoEntrega,
                itens.stream()
                        .map(itemDto -> {
                            EstoqueEntity estoque = estoquesDaLoja.stream()
                                    .filter(e -> e.getProduto().getId().equals(itemDto.produtoId()))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque"));

                            return new ItemPedido(
                                    estoque.getProduto().getName(),           // nome do produto
                                    new Preco(estoque.getPrecoVenda()),       // preço unitário
                                    itemDto.quantidade()                      // quantidade do pedido
                            );
                        })
                        .toList(),
                formaDePagamento
        );
    }
}