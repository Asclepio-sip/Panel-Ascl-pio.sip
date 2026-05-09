package projeto_base_de_telas_e_login.domain.model.Preco;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Preco {

    private BigDecimal valor;
    private BigDecimal desconto;

    public Preco(BigDecimal valor) {
        this.valor = valor;
        this.desconto = BigDecimal.ZERO;
    }

    public Preco(BigDecimal valor, BigDecimal desconto) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor inválido");
        }

        if (desconto != null && desconto.compareTo(valor) > 0) {
            throw new IllegalArgumentException("Desconto não pode ser maior que o valor");
        }

        this.valor = valor.setScale(2, RoundingMode.HALF_EVEN);
        this.desconto = desconto != null
                ? desconto.setScale(2, RoundingMode.HALF_EVEN)
                : BigDecimal.ZERO;
    }

    public boolean temDesconto() {
        return desconto.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getValorFinal() {
        BigDecimal resultado = temDesconto()
                ? valor.subtract(desconto)
                : valor;

        return resultado.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public Preco multiplicar(int quantidade) {
        BigDecimal resultado = getValorFinal()
                .multiply(BigDecimal.valueOf(quantidade));

        return new Preco(resultado.setScale(2, RoundingMode.HALF_EVEN));
    }

    public Preco somar(Preco outro) {
        BigDecimal resultado = this.getValorFinal()
                .add(outro.getValorFinal());

        return new Preco(resultado.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Override
    public String toString() {
        return "R$ " + getValorFinal().setScale(2, RoundingMode.HALF_EVEN);
    }
}