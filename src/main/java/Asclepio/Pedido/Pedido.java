package Asclepio.Pedido;

import jakarta.persistence.*;

import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Enum.TipoEntrega;
import Asclepio.ItemPedido.ItemPedido;
import Asclepio.Loja.Loja.Loja;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PED_ID")
    private Long id;

    @Column(name = "PED_CRIADO_EM", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "PED_NOME_CLIENTE", nullable = false, length = 150)
    private String nomeCliente;

    @Column(name = "PED_EMAIL", length = 150)
    private String email;

    @Column(name = "PED_TELEFONE", length = 20)
    private String telefone;

    @Column(name = "PED_ENDERECO", length = 255)
    private String endereco;

    @Column(name = "PED_BAIRRO", length = 100)
    private String bairro;

    @Column(name = "PED_COMPLEMENTO", length = 255)
    private String complemento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PED_LOJA_ID")
    private Loja loja;

    @Column(name = "PED_OBSERVACAO", length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "PED_STATUS", nullable = false, length = 30)
    private StatusDoPedido status = StatusDoPedido.AGUARDANDO;

    @Enumerated(EnumType.STRING)
    @Column(name = "PED_TIPO_ENTREGA", length = 30)
    private TipoEntrega tipoEntrega;

    @Column(name = "PED_TOTAL_PRODUTOS", precision = 10, scale = 2)
    private BigDecimal totalProdutos;

    @Column(name = "PED_VALOR_FRETE", precision = 10, scale = 2)
    private BigDecimal valorFrete;

    @Column(name = "PED_TOTAL_FINAL", precision = 10, scale = 2)
    private BigDecimal totalFinal;

    @Column(name = "PED_FRETE_GRATIS")
    private Boolean freteGratis;

    @Enumerated(EnumType.STRING)
    @Column(name = "PED_FORMA_PAGAMENTO", length = 30)
    private FormaDePagamento formaDePagamento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    @Column(name = "PED_CODIGO_RASTREIO", nullable = false, unique = true, length = 40)
    private String codigoRastreio;

    @Column(name = "PED_CONCLUIDO_EM")
    private LocalDateTime concluidoEm;

    public Pedido() {
    }

    public Pedido(Long id, Long lojaId, String nomeCliente, String email, String telefone, String endereco, String bairro, String complemento, String observacao, TipoEntrega tipoEntrega, List<ItemPedido> itens, FormaDePagamento formaDePagamento, String codigoRastreio, LocalDateTime concluidoEm) {

        if (itens == null || itens.isEmpty()) {
            throw new RuntimeException("Pedido precisa ter itens");
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

        this.codigoRastreio = codigoRastreio;

        this.concluidoEm = concluidoEm;

        calcularTotais();
    }

    public void calcularTotais() {

        this.totalProdutos = itens.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorFrete = BigDecimal.ZERO;

        this.totalFinal = totalProdutos;
    }

    public void calcularTotalFinal() {

        BigDecimal frete = valorFrete != null ? valorFrete : BigDecimal.ZERO;

        this.totalFinal = totalProdutos.add(frete);
    }

    public void aplicarFrete(BigDecimal frete) {

        this.valorFrete = frete;

        this.totalFinal = totalProdutos.add(frete);
    }

    public void calcularSubtotalProdutos() {

        this.totalProdutos = itens.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public void setStatus(StatusDoPedido status) {
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

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public LocalDateTime getConcluidoEm() {
        return concluidoEm;
    }

    public void setConcluidoEm(LocalDateTime concluidoEm) {
        this.concluidoEm = concluidoEm;
    }
}