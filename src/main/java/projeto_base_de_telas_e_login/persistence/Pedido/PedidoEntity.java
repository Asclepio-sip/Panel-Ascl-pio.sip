package projeto_base_de_telas_e_login.persistence.Pedido;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.model.Preco.Preco;
import projeto_base_de_telas_e_login.persistence.ItemPedido.ItemPedidoEntity;
import projeto_base_de_telas_e_login.model.Pedido.Enum.FormaDePagamento;
import projeto_base_de_telas_e_login.model.Pedido.Enum.StatusDoPedido;
import projeto_base_de_telas_e_login.model.Pedido.Enum.TipoEntrega;
import projeto_base_de_telas_e_login.model.Pedido.Pedido;
import projeto_base_de_telas_e_login.model.Loja.Loja;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lojaId;

    private LocalDateTime criadoEm;

    private String nomeCliente;
    private String email;
    private String telefone;

    private String endereco;
    private String bairro;

    private String complemento;

    @Column(length = 500)
    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusDoPedido status;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipoEntrega;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalProdutos;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorFrete;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalFinal;

    private Boolean freteGratis;

    @Enumerated(EnumType.STRING)
    private FormaDePagamento formaDePagamento;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemPedidoEntity> itens;

    protected PedidoEntity() {}

    public PedidoEntity(Pedido pedido) {

        this.id = pedido.getId();
        this.lojaId = pedido.getLoja().getId();
        this.criadoEm = pedido.getCriadoEm();
        this.nomeCliente = pedido.getNomeCliente();
        this.email = pedido.getEmail();
        this.telefone = pedido.getTelefone();
        this.endereco = pedido.getEndereco();
        this.bairro = pedido.getBairro();
        this.complemento = pedido.getComplemento();
        this.observacao = pedido.getObservacao();
        this.status = pedido.getStatus();
        this.tipoEntrega = pedido.getTipoEntrega();

        // 🔥 ESTAVA FALTANDO ISSO
        this.formaDePagamento = pedido.getFormaPagamento();

        this.totalProdutos = pedido.getTotalProdutos().getValor();
        this.valorFrete = pedido.getValorFrete().getValor();
        this.totalFinal = pedido.getTotalFinal().getValor();
        this.freteGratis = pedido.getFreteGratis();

        if (pedido.getItens() != null) {
            this.itens = pedido.getItens()
                    .stream()
                    .map(item -> new ItemPedidoEntity(item, this))
                    .toList();
        }
    }
    // ENTITY → DOMAIN
    public Pedido toDomain() {

        var itensDomain = this.itens.stream()
                .map(ItemPedidoEntity::toDomain)
                .toList();

        var lojaStub = new Loja(
                this.lojaId,
                null,
                null
        );

        Pedido pedido = new Pedido(
                lojaStub,
                this.nomeCliente,
                this.email,
                this.telefone,
                this.endereco,
                this.bairro,
                this.complemento,
                this.observacao,
                this.tipoEntrega,
                itensDomain,
                this.formaDePagamento
        );

        // 🔥 SETANDO ID E DATA CORRETAMENTE
        pedido.setId(this.id);
        pedido.setCriadoEm(this.criadoEm);
        pedido.setStatus(this.status);

        pedido.aplicarFrete(
                new Preco(this.valorFrete)
        );

        return pedido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLojaId() {
        return lojaId;
    }

    public void setLojaId(Long lojaId) {
        this.lojaId = lojaId;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public StatusDoPedido getStatus() {
        return status;
    }

    public void setStatus(StatusDoPedido status) {
        this.status = status;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public BigDecimal getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(BigDecimal totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(BigDecimal totalFinal) {
        this.totalFinal = totalFinal;
    }

    public Boolean getFreteGratis() {
        return freteGratis;
    }

    public void setFreteGratis(Boolean freteGratis) {
        this.freteGratis = freteGratis;
    }

    public List<ItemPedidoEntity> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoEntity> itens) {
        this.itens = itens;
    }
}