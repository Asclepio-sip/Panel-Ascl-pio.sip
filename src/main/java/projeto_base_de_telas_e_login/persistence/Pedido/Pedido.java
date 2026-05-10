package projeto_base_de_telas_e_login.persistence.Pedido;

import jakarta.persistence.*;

import projeto_base_de_telas_e_login.model.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.model.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.model.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.persistence.ItemPedido.ItemPedido;
import projeto_base_de_telas_e_login.persistence.Loja.loja.Loja;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private LocalDateTime criadoEm;

    private String nomeCliente;

    private String email;

    private String telefone;

    private String endereco;

    private String bairro;

    private String complemento;

    @ManyToOne
    @JoinColumn(name = "loja_id")
    private Loja loja;

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusDoPedido status;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipoEntrega;

    @Column(
            precision = 10,
            scale = 2
    )
    private BigDecimal totalProdutos;

    @Column(
            precision = 10,
            scale = 2
    )
    private BigDecimal valorFrete;

    @Column(
            precision = 10,
            scale = 2
    )
    private BigDecimal totalFinal;

    private Boolean freteGratis;

    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemPedido> itens;

    public Pedido() {
    }

    public Pedido(
            Long id,
            Long lojaId,
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
            throw new RuntimeException(
                    "Pedido precisa ter itens"
            );
        }

        this.id = id;

        this.criadoEm = LocalDateTime.now();

        this.nomeCliente = nomeCliente;

        this.email = email;

        this.telefone = telefone;

        this.endereco = endereco;

        this.bairro = bairro;

        this.complemento = complemento;

        this.observacao = observacao;

        this.tipoEntrega = tipoEntrega;

        this.itens = itens;

        this.formaDePagamento = formaDePagamento;

        this.status = StatusDoPedido.AGUARDANDO;

        calcularTotais();
    }

    public void calcularTotais() {

        this.totalProdutos = itens
                .stream()
                .map(ItemPedido::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        this.valorFrete = BigDecimal.ZERO;

        this.totalFinal = totalProdutos;
    }

    public void aplicarFrete(
            BigDecimal frete
    ) {

        this.valorFrete = frete;

        this.totalFinal =
                totalProdutos.add(frete);
    }

    public Long getId() {
        return id;
    }



    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getObservacao() {
        return observacao;
    }

    public StatusDoPedido getStatus() {
        return status;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public BigDecimal getTotalProdutos() {
        return totalProdutos;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public Boolean getFreteGratis() {
        return freteGratis;
    }

    public FormaDePagamento getFormaDePagamento() {
        return formaDePagamento;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setStatus(
            StatusDoPedido status
    ) {
        this.status = status;
    }


    public void setId(Long id) {
        this.id = id;
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

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public void setTotalProdutos(BigDecimal totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public void setTotalFinal(BigDecimal totalFinal) {
        this.totalFinal = totalFinal;
    }

    public void setFreteGratis(Boolean freteGratis) {
        this.freteGratis = freteGratis;
    }

    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}