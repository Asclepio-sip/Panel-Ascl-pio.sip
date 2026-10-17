package Asclepio.Estoque.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.DecimalMin;
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

        Long variacaoId,

        // Opcionais
        @DecimalMin("0.0")
        BigDecimal precoCusto,

        @Min(0)
        Integer estoqueMinimo,

        // null = mostra no catálogo online
        Boolean mostrarNoCatalogo


) {
}