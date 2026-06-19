package Asclepio.Loja.LojaBairro;

import Asclepio.Loja.Bairro.Bairro;
import Asclepio.Loja.Loja.Loja;
import Asclepio.exception.BusinessException;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_LOJA_BAIRRO",

        uniqueConstraints = {

                @UniqueConstraint(

                        name = "UK_LOJB_LOJA_BAIRRO",

                        columnNames = {"LJB_LOJA_ID", "LJB_BAIRRO_ID"})})
public class LojaBairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LJB_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LJB_LOJA_ID", nullable = false)
    private Loja loja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LJB_BAIRRO_ID", nullable = false)
    private Bairro bairro;

    @Column(name = "LJB_VALOR_FRETE", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFrete;

    protected LojaBairro() {
    }

    public LojaBairro(Long id, Loja loja, Bairro bairro, BigDecimal valorFrete) {

        validarFrete(valorFrete);

        this.id = id;
        this.loja = loja;
        this.bairro = bairro;
        this.valorFrete = valorFrete;
    }

    public void atualizarFrete(BigDecimal valorFrete) {

        validarFrete(valorFrete);

        this.valorFrete = valorFrete;
    }

    public boolean possuiFreteGratis() {

        return valorFrete != null && valorFrete.compareTo(BigDecimal.ZERO) == 0;
    }

    private void validarFrete(BigDecimal valorFrete) {

        if (valorFrete == null) {

            throw new BusinessException("Valor do frete é obrigatório");
        }

        if (valorFrete.compareTo(BigDecimal.ZERO) < 0) {

            throw new BusinessException("Valor do frete não pode ser negativo");
        }
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public void setValorFrete(BigDecimal valorFrete) {

        validarFrete(valorFrete);

        this.valorFrete = valorFrete;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof LojaBairro lojaBairro)) {
            return false;
        }

        return id != null && id.equals(lojaBairro.id);
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

    @Override
    public String toString() {

        return "LojaBairro{" + "id=" + id + ", valorFrete=" + valorFrete + '}';
    }
}