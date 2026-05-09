package projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.pdfDTO;

import java.util.List;

public class PedidoPdfDTO {

    private Long id;
    private String criado;
    private String cliente;
    private String telefone;
    private String endereco;
    private String bairro;
    private String complemento;
    private String formaPagamento;
    private String total;

    private List<ItemPedidoPdfDTO> itens;

    public PedidoPdfDTO(Long id, String string, String cliente, String telefone, String endereco, String string1, String complemento, String string2, String string3, List<ItemPedidoPdfDTO> itens) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCriado() {
        return criado;
    }

    public void setCriado(String criado) {
        this.criado = criado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ItemPedidoPdfDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoPdfDTO> itens) {
        this.itens = itens;
    }
}