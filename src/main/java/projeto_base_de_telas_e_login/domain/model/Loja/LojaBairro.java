package projeto_base_de_telas_e_login.domain.model.Loja;

import java.math.BigDecimal;

public class LojaBairro {

    private Loja loja;
    private Bairro bairro;
    private BigDecimal valorFrete;

    public LojaBairro(Loja loja, Bairro bairro, BigDecimal valorFrete) {

        if (valorFrete == null || valorFrete.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor de frete inválido.");
        }

        this.loja = loja;
        this.bairro = bairro;
        this.valorFrete = valorFrete;
    }

    public Loja getLoja() { return loja; }
    public Bairro getBairro() { return bairro; }
    public BigDecimal getValorFrete() { return valorFrete; }

    public void alterarFrete(BigDecimal novoValor) {
        if (novoValor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor de frete inválido.");
        }
        this.valorFrete = novoValor;
    }
}