package projeto_base_de_telas_e_login.MovimentacaoEstoque;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.Estoque.Estoque;
import projeto_base_de_telas_e_login.Loja.Loja.Loja;
import projeto_base_de_telas_e_login.MovimentacaoEstoque.Enum.TipoMovimentacaoEstoque;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.Usuario.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Estoque estoque;

    @ManyToOne
    private Loja loja;

    @ManyToOne
    private Product produto;

    @ManyToOne
    private User usuario;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacaoEstoque tipo;

    private Integer quantidadeAntes;

    private Integer quantidadeDepois;

    private BigDecimal precoAntes;

    private BigDecimal precoDepois;

    private BigDecimal descontoAntes;

    private BigDecimal descontoDepois;

    private LocalDateTime criadoEm;

    @Column(length = 500)
    private String observacao;

    protected MovimentacaoEstoque() {}

    public MovimentacaoEstoque(
            Estoque estoque,
            Loja loja,
            Product produto,
            User usuario,
            TipoMovimentacaoEstoque tipo,
            Integer quantidadeAntes,
            Integer quantidadeDepois,
            BigDecimal precoAntes,
            BigDecimal precoDepois,
            BigDecimal descontoAntes,
            BigDecimal descontoDepois,
            String observacao
    ) {
        this.estoque = estoque;
        this.loja = loja;
        this.produto = produto;
        this.usuario = usuario;
        this.tipo = tipo;
        this.quantidadeAntes = quantidadeAntes;
        this.quantidadeDepois = quantidadeDepois;
        this.precoAntes = precoAntes;
        this.precoDepois = precoDepois;
        this.descontoAntes = descontoAntes;
        this.descontoDepois = descontoDepois;
        this.criadoEm = LocalDateTime.now();
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public Loja getLoja() {
        return loja;
    }

    public Product getProduto() {
        return produto;
    }

    public User getUsuario() {
        return usuario;
    }

    public TipoMovimentacaoEstoque getTipo() {
        return tipo;
    }

    public Integer getQuantidadeAntes() {
        return quantidadeAntes;
    }

    public Integer getQuantidadeDepois() {
        return quantidadeDepois;
    }

    public BigDecimal getPrecoAntes() {
        return precoAntes;
    }

    public BigDecimal getPrecoDepois() {
        return precoDepois;
    }

    public BigDecimal getDescontoAntes() {
        return descontoAntes;
    }

    public BigDecimal getDescontoDepois() {
        return descontoDepois;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}