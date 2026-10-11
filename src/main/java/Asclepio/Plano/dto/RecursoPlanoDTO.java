package Asclepio.Plano.dto;

import Asclepio.Plano.RecursoPlano;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecursoPlanoDTO(
        @NotBlank @Size(max = 100)
        String nome,

        boolean incluido,

        @Size(max = 100)
        String detalhe
) {

    public static RecursoPlanoDTO de(RecursoPlano r) {
        return new RecursoPlanoDTO(r.getNome(), r.isIncluido(), r.getDetalhe());
    }

    public RecursoPlano paraEntidade() {
        return new RecursoPlano(nome.trim(), incluido, detalhe == null ? "" : detalhe.trim());
    }
}
