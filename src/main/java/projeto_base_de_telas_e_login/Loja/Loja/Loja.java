package projeto_base_de_telas_e_login.Loja.Loja;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.Loja.Bairro.Enum.TipoAtendimentoLoja;
import projeto_base_de_telas_e_login.Loja.LojaBairro.LojaBairro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loja")
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoAtendimentoLoja tipoAtendimento;

    private BigDecimal valorMinimoFreteGratis;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LojaBairro> bairros = new ArrayList<>();

    public Loja() {
    }

    public Loja(Long id, String nome, TipoAtendimentoLoja tipoAtendimento) {
        this.id = id;
        this.nome = nome;
        this.tipoAtendimento = tipoAtendimento;
    }

    public boolean aceitaEntrega() {
        return tipoAtendimento == TipoAtendimentoLoja.ENTREGA || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public boolean aceitaRetirada() {
        return tipoAtendimento == TipoAtendimentoLoja.RETIRADA || tipoAtendimento == TipoAtendimentoLoja.AMBOS;
    }

    public void configurarFreteGratis(BigDecimal valorMinimo) {

        if (valorMinimo != null && valorMinimo.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException("Valor mínimo inválido");
        }

        this.valorMinimoFreteGratis = valorMinimo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoAtendimentoLoja getTipoAtendimento() {
        return tipoAtendimento;
    }

    public BigDecimal getValorMinimoFreteGratis() {
        return valorMinimoFreteGratis;
    }

    public List<LojaBairro> getBairros() {
        return bairros;
    }

    public void setId(Long id) {
        this.id = id;
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