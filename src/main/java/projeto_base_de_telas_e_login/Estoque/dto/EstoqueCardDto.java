package projeto_base_de_telas_e_login.Estoque.dto;

import projeto_base_de_telas_e_login.Estoque.Estoque;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record EstoqueCardDto(

        Long id,
        String nomeProduto,
        String imagemBase64,
        String variacao,
        String nomeLoja,
        Integer quantidade,
        BigDecimal valorFinal

) {

    public static EstoqueCardDto from(Estoque e) {

        BigDecimal desconto = e.getPercentualDesconto() != null
                ? e.getPercentualDesconto()
                : BigDecimal.ZERO;

        BigDecimal valorDesconto = e.getPrecoVenda()
                .multiply(desconto)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal finalValue = e.getPrecoVenda().subtract(valorDesconto);

        return new EstoqueCardDto(
                e.getId(),
                e.getProduto().getName(),
                e.getProduto().getImagemBase64(),
                e.getProduto().getVariacao(),
                e.getLoja().getNome(),
                e.getQuantidade(),
                finalValue
        );
    }
}