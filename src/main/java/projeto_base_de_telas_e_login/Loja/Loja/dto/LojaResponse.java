package projeto_base_de_telas_e_login.Loja.Loja.dto;

import projeto_base_de_telas_e_login.Loja.Loja.Loja;

import java.math.BigDecimal;

public record LojaResponse(

        Long id,
        String nome,
        String tipoAtendimento,
        BigDecimal valorMinimoFreteGratis

) {

    public static LojaResponse fromEntity(
            Loja loja
    ) {

        return new LojaResponse(
                loja.getId(),
                loja.getNome(),
                loja.getTipoAtendimento().name(),
                loja.getValorMinimoFreteGratis()
        );
    }
}