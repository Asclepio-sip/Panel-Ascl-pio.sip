package Asclepio.Estoque.dto;

import java.math.BigDecimal;

public record AtulizarResquet(

        Long lojaID,

        String nomeLoja,

        Long produtoId,

        String nomeProduto,

        Integer quantidade,

        BigDecimal precoVenda) {
}
