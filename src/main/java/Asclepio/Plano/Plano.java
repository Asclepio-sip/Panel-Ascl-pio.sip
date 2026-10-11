package Asclepio.Plano;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Plano de assinatura do Asclépio (Free, Start, Growth, Pro...).
 * Quem manda é o painel SaaS: ele cria/edita lá e espelha aqui via /interno/planos.
 * Aqui os planos são só lidos (GET /planos, público, usado na landing page).
 */
@Entity
@Table(name = "TB_PLANO")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLA_ID")
    private Long id;

    /** Id do plano no painel SaaS (é o id exposto no GET /planos). */
    @Column(name = "PLA_SAAS_ID", nullable = false, unique = true)
    private Long saasId;

    @Column(name = "PLA_NOME", nullable = false, length = 50)
    private String nome;

    @Column(name = "PLA_DESCRICAO", nullable = false)
    private String descricao = "";

    /** Preço mensal em reais. */
    @Column(name = "PLA_PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "PLA_MAIS_POPULAR", nullable = false)
    private boolean maisPopular;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TB_PLANO_RECURSO", joinColumns = @JoinColumn(name = "PRE_PLANO_ID"))
    @OrderColumn(name = "PRE_ORDEM")
    private List<RecursoPlano> recursos = new ArrayList<>();

    @Column(name = "PLA_ATUALIZADO_EM", nullable = false)
    private LocalDateTime atualizadoEm;

    protected Plano() {
    }

    public Plano(Long saasId) {
        this.saasId = saasId;
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, boolean maisPopular,
                          List<RecursoPlano> recursos) {
        this.nome = nome;
        this.descricao = descricao == null ? "" : descricao;
        this.preco = preco;
        this.maisPopular = maisPopular;
        this.recursos.clear();
        this.recursos.addAll(recursos);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void tirarMaisPopular() {
        this.maisPopular = false;
    }

    public Long getId() {
        return id;
    }

    public Long getSaasId() {
        return saasId;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public boolean isMaisPopular() {
        return maisPopular;
    }

    public List<RecursoPlano> getRecursos() {
        return recursos;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
