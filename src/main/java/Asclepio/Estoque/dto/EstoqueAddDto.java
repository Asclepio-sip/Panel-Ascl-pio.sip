package Asclepio.Estoque.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EstoqueAddDto(

        Long lojaID,

        String nomeLoja,

        Long produtoId,

        String nomeProduto,

        @NotNull
        @Min(0)
        Integer quantidade,

        @NotNull
        BigDecimal precoVenda,

        Long variacaoId


) {
}