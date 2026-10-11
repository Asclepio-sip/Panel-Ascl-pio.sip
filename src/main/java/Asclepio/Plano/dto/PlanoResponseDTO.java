package Asclepio.Plano.dto;

import Asclepio.Plano.Plano;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record PlanoResponseDTO(
        @Schema(description = "Mesmo id do plano no painel SaaS", example = "3")
        Long id,
        @Schema(example = "Growth")
        String nome,
        @Schema(example = "Para farmácias em crescimento")
        String descricao,
        @Schema(description = "Preço mensal em reais", example = "250.00")
        BigDecimal preco,
        @Schema(description = "Selo \"Mais Popular\" (só um plano tem)")
        boolean maisPopular,
        @Schema(description = "Na ordem da tabela de comparação")
        List<RecursoPlanoDTO> recursos
) {

    public static PlanoResponseDTO de(Plano p) {
        return new PlanoResponseDTO(
                p.getSaasId(),
                p.getNome(),
                p.getDescricao(),
                p.getPreco(),
                p.isMaisPopular(),
                p.getRecursos().stream().map(RecursoPlanoDTO::de).toList()
        );
    }
}
