package projeto_base_de_telas_e_login.adapter.out.persistence.Loja.loja;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.adapter.out.persistence.Loja.LojaBairro.LojaBairroEntity;
import projeto_base_de_telas_e_login.domain.model.Loja.Loja;
import projeto_base_de_telas_e_login.domain.model.Loja.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loja")
public class LojaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoAtendimentoLoja tipoAtendimento;

    private BigDecimal valorMinimoFreteGratis;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LojaBairroEntity> bairros = new ArrayList<>();

    protected LojaEntity() {}

    // DOMAIN -> ENTITY
    public LojaEntity(Loja loja) {
        this.id = loja.getId();
        this.nome = loja.getNome();
        this.tipoAtendimento = loja.getTipoAtendimento();
        this.valorMinimoFreteGratis = loja.getValorMinimoFreteGratis();
    }

    // ENTITY -> DOMAIN
    public Loja toDomain() {
        Loja loja = new Loja(id, nome, tipoAtendimento);
        loja.configurarFreteGratis(valorMinimoFreteGratis);

        if (bairros != null && loja.aceitaEntrega()) { // <-- importante
            bairros.forEach(b ->
                    loja.adicionarBairro(
                            b.getBairro().toDomain(),
                            b.getValorFrete()
                    )
            );
        }

        return loja;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public TipoAtendimentoLoja getTipoAtendimento() { return tipoAtendimento; }
    public BigDecimal getValorMinimoFreteGratis() { return valorMinimoFreteGratis; }
    public List<LojaBairroEntity> getBairros() { return bairros; }

    public boolean aceitaEntrega() {
        return this.tipoAtendimento == TipoAtendimentoLoja.ENTREGA
                || this.tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipoAtendimento(TipoAtendimentoLoja tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public void setValorMinimoFreteGratis(BigDecimal valorMinimoFreteGratis) {
        this.valorMinimoFreteGratis = valorMinimoFreteGratis;
    }
}