package projeto_base_de_telas_e_login.dto.Estoque;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AplicarPromocaoDto(

        @NotNull
        Long lojaId,

        @NotNull
        Long produtoId,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("100.0")
        BigDecimal percentual

) {
}