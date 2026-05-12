package projeto_base_de_telas_e_login.Loja.LojaBairro;

import jakarta.persistence.*;
import projeto_base_de_telas_e_login.Loja.Bairro.Bairro;
import projeto_base_de_telas_e_login.Loja.Loja.Loja;

import java.math.BigDecimal;

@Entity
@Table(name = "loja_bairro")
public class LojaBairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    private BigDecimal valorFrete;

    public LojaBairro() {
    }

    public LojaBairro(Long id, Loja loja, Bairro bairro, BigDecimal valorFrete) {

        if (valorFrete == null || valorFrete.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException("Valor de frete inválido");
        }

        this.id = id;
        this.loja = loja;
        this.bairro = bairro;
        this.valorFrete = valorFrete;
    }

    public Long getId() {
        return id;
    }

    public Loja getLoja() {
        return loja;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {

        if (valorFrete.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Valor de frete inválido");
        }

        this.valorFrete = valorFrete;
    }
}