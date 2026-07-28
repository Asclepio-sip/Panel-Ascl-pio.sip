package Asclepio.CadastroProduto.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
        BigDecimal precoVenda

) {
}