package projeto_base_de_telas_e_login.Estoque.dto;

import projeto_base_de_telas_e_login.Estoque.Estoque;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record EstoqueListaDto(

        Long id,
        Long lojaId,
        String nomeLoja,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoVenda,
        BigDecimal percentualDesconto,
        BigDecimal valorFinal

) {

    public static EstoqueListaDto fromDomain(Estoque estoque) {

        BigDecimal desconto = estoque.getPercentualDesconto() != null
                ? estoque.getPercentualDesconto()
                : BigDecimal.ZERO;

        BigDecimal valorDesconto = estoque.getPrecoVenda()
                .multiply(desconto)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal valorFinal = estoque.getPrecoVenda()
                .subtract(valorDesconto);

        return new EstoqueListaDto(

                estoque.getId(),
                estoque.getLoja().getId(),
                estoque.getLoja().getNome(),
                estoque.getProduto().getId(),
                estoque.getProduto().getName(),
                estoque.getQuantidade(),
                estoque.getPrecoVenda(),
                desconto,
                valorFinal
        );
    }
}