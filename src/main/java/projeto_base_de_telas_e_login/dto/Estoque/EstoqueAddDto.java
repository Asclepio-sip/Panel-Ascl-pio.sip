package projeto_base_de_telas_e_login.dto.Estoque;

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
        BigDecimal precoVenda

) {
}