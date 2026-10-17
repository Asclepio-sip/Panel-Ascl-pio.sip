package Asclepio.CadastroProduto.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record CadastroEstoqueDTO(

        @NotNull
        Long lojaId,

        @NotNull
        Long variacaoId,

        @NotNull
        @Min(0)
        Integer quantidade,

        @NotNull
        BigDecimal precoVenda,

        // Opcionais
        @DecimalMin("0.0")
        BigDecimal precoCusto,

        @Min(0)
        Integer estoqueMinimo,

        // null = mostra no catálogo online
        Boolean mostrarNoCatalogo

) {
}