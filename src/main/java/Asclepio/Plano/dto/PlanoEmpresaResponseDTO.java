package Asclepio.Plano.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** O que o plano da empresa logada permite e quanto já foi usado (GET /planos/atual). */
public record PlanoEmpresaResponseDTO(
        @Schema(description = "Nome do plano; null = empresa sem plano (sem limites)", example = "Free")
        String plano,
        UsoLimiteDTO lojas,
        UsoLimiteDTO funcionarios,
        @Schema(description = "Plano inclui o cadastro de clientes")
        boolean cadastroClientes
) {

    public record UsoLimiteDTO(
            @Schema(example = "1")
            long usado,
            @Schema(description = "null = sem limite", example = "1")
            Integer limite,
            @Schema(description = "true quando não dá para cadastrar mais")
            boolean atingido
    ) {
        public static UsoLimiteDTO de(long usado, Integer limite) {
            return new UsoLimiteDTO(usado, limite, limite != null && usado >= limite);
        }
    }
}
