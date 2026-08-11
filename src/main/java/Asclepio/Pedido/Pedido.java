package Asclepio.Pedido;

import Asclepio.ClienteEmpresa.ClienteEmpresa;
import Asclepio.Empresa.Empresa;
import Asclepio.ItemPedido.ItemPedido;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.Pedido.Enum.StatusDoPedido;
import Asclepio.Pedido.Enum.TipoAtendimentoPedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private StatusDoPedido status;

    @Column(name = "PED_TOTAL_PRODUTOS", precision = 10, scale = 2)
    private BigDecimal totalProdutos = BigDecimal.ZERO;

    @Column(name = "PED_VALOR_FRETE", precision = 10, scale = 2)
    private BigDecimal valorFrete = BigDecimal.ZERO;

    @Column(name = "PED_TOTAL_FINAL", precision = 10, scale = 2)
    private BigDecimal totalFinal = BigDecimal.ZERO;

    @Column(name = "PED_FRETE_GRATIS")
    private Boolean freteGratis = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "PED_FORMA_PAGAMENTO", length = 30)
    private FormaDePagamento formaDePagamento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Column(name = "PED_CODIGO_RASTREIO", nullable = false, unique = true, length = 40)
    private String codigoRastreio;

    @Column(name = "PED_CONCLUIDO_EM")
    private LocalDateTime concluidoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PED_EMPRESA_ID", nullable = false)
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "PED_TIPO_ATENDIMENTO", nullable = false, length = 40)
    private TipoAtendimentoPedido tipoAtendimentoPedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PED_CLIENTE_ID", nullable = true)
    private ClienteEmpresa cliente;


    public Pedido() {
    }

    public void calcularSubtotalProdutos() {
        if (itens == null || itens.isEmpty()) {
            this.totalProdutos = BigDecimal.ZERO;
            return;
        }

        this.totalProdutos = itens.stream().map(ItemPedido::getSubtotal).filter(subtotal -> subtotal != null).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void definirStatusInicial() {
        if (tipoAtendimentoPedido == TipoAtendimentoPedido.BALCAO) {
            status = StatusDoPedido.SEPARACAO;
        } else {
            status = StatusDoPedido.AGUARDANDO;
        }
    }

    public void calcularTotalFinal() {
        BigDecimal produtos = totalProdutos != null ? totalProdutos : BigDecimal.ZERO;
        BigDecimal frete = valorFrete != null ? valorFrete : BigDecimal.ZERO;

        this.totalFinal = produtos.add(frete);
    }

    public void aplicarFrete(BigDecimal frete) {
        this.valorFrete = frete != null ? frete : BigDecimal.ZERO;
        calcularTotalFinal();
    }

    public void zerarEntrega() {
        this.endereco = null;
        this.bairro = null;
        this.complemento = null;
        this.valorFrete = BigDecimal.ZERO;
        this.freteGratis = false;
        calcularTotalFinal();
    }


    public void adicionarItem(ItemPedido item) {
        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }

        this.itens.add(item);
    }


    public ClienteEmpresa getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEmpresa cliente) {
        this.cliente = cliente;
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

    public Loja getLoja() {
        return loja;
    }

    public String getObservacao() {
        return observacao;
    }

    public StatusDoPedido getStatus() {
        return status;
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

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public LocalDateTime getConcluidoEm() {
        return concluidoEm;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public TipoAtendimentoPedido getTipoAtendimentoPedido() {
        return tipoAtendimentoPedido;
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

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setStatus(StatusDoPedido status) {
        this.status = status;
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
        this.itens = itens != null ? itens : new ArrayList<>();
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public void setConcluidoEm(LocalDateTime concluidoEm) {
        this.concluidoEm = concluidoEm;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setTipoAtendimentoPedido(TipoAtendimentoPedido tipoAtendimentoPedido) {
        this.tipoAtendimentoPedido = tipoAtendimentoPedido;
    }


}