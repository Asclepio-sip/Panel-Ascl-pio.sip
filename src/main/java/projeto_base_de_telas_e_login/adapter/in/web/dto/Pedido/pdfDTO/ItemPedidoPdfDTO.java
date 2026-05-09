package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.pdfDTO;

public class ItemPedidoPdfDTO {

    private String nomeProduto;
    private String imagemUrl;
    private Integer quantidade;
    private String precoUnitario;
    private String subtotal;

    public ItemPedidoPdfDTO(String name, String imagemUrl, Integer quantidade, String string, String string1) {
    }

    // getters

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(String precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}