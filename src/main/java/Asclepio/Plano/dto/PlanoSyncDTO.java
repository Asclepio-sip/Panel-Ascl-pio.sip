package Asclepio.Plano.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/**
 * Plano enviado pelo painel SaaS (POST /interno/planos e PUT /interno/planos/{saasId}).
 * No PUT o saasId vem da URL e o do corpo é ignorado.
 */
public record PlanoSyncDTO(
        Long saasId,

        @NotBlank @Size(max = 50)
        String nome,

        @Size(max = 255)
        String descricao,

        @NotNull @DecimalMin("0.0")
        BigDecimal preco,

        boolean maisPopular,

        List<@Valid RecursoPlanoDTO> recursos
) {
}
