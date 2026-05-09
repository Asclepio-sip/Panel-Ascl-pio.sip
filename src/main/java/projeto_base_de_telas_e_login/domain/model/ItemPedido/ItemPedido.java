package projeto_base_de_telas_e_login.domain.model.ItemPedido;

import projeto_base_de_telas_e_login.domain.model.Preco.Preco;

public class ItemPedido {

    private String nomeProduto;
    private Preco precoUnitario;
    private Integer quantidade;
    private Preco subtotal;

    public ItemPedido(String nomeProduto, Preco precoUnitario, Integer quantidade) {

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }

        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.subtotal = precoUnitario.multiplicar(quantidade);
    }

    public String getNomeProduto() { return nomeProduto; }
    public Preco getPrecoUnitario() { return precoUnitario; }
    public Integer getQuantidade() { return quantidade; }
    public Preco getSubtotal() { return subtotal; }
}