package projeto_base_de_telas_e_login.domain.model.Pedido;

import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import projeto_base_de_telas_e_login.domain.model.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.domain.model.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.domain.model.Preco.Preco;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private Long id;
    private Loja loja;
    private LocalDateTime criadoEm;

    private String nomeCliente;
    private String email;
    private String telefone;

    private String endereco;
    private String bairro;
    private String complemento;
    private String observacao;

    private StatusDoPedido status;
    private TipoEntrega tipoEntrega;

    private List<ItemPedido> itens;

    private Preco totalProdutos;
    private Preco valorFrete;
    private Preco totalFinal;

    private Boolean freteGratis;
    private FormaDePagamento formaDePagamento;

    public Pedido(
            Loja loja,
            String nomeCliente,
            String email,
            String telefone,
            String endereco,
            String bairro,
            String complemento,
            String observacao,
            TipoEntrega tipoEntrega,
            List<ItemPedido> itens,
            FormaDePagamento formaDePagamento
    ) {

        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido precisa ter pelo menos um item");
        }

        this.loja = loja;
        this.criadoEm = LocalDateTime.now();
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.bairro = bairro;
        this.complemento = complemento;
        this.observacao = observacao;
        this.tipoEntrega = tipoEntrega;

        this.status = StatusDoPedido.AGUARDANDO;

        this.itens = itens;
        this.formaDePagamento = formaDePagamento;

        calcularTotais();
    }

    private void calcularTotais() {

        this.totalProdutos = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(new Preco(java.math.BigDecimal.ZERO), Preco::somar);

        this.valorFrete = new Preco(java.math.BigDecimal.ZERO);

        this.totalFinal = totalProdutos;
    }

    public void aplicarFrete(Preco frete) {
        this.valorFrete = frete;
        this.totalFinal = totalProdutos.somar(frete);
    }

    // GETTERS


    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setStatus(StatusDoPedido status) {
        this.status = status;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setTotalProdutos(Preco totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public void setValorFrete(Preco valorFrete) {
        this.valorFrete = valorFrete;
    }

    public void setTotalFinal(Preco totalFinal) {
        this.totalFinal = totalFinal;
    }

    public void setFreteGratis(Boolean freteGratis) {
        this.freteGratis = freteGratis;
    }

    public FormaDePagamento getFormaPagamento() { return formaDePagamento; }
    public Long getId() { return id; }
    public Loja getLoja() { return loja; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public String getNomeCliente() { return nomeCliente; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public String getBairro() { return bairro; }
    public String getComplemento() { return complemento; }
    public String getObservacao() { return observacao; }
    public StatusDoPedido getStatus() { return status; }
    public TipoEntrega getTipoEntrega() { return tipoEntrega; }
    public List<ItemPedido> getItens() { return itens; }
    public Preco getTotalProdutos() { return totalProdutos; }
    public Preco getValorFrete() { return valorFrete; }
    public Preco getTotalFinal() { return totalFinal; }
    public Boolean getFreteGratis() { return freteGratis; }
}