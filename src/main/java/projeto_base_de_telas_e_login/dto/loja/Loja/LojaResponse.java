package projeto_base_de_telas_e_login.dto.loja.Loja;

import projeto_base_de_telas_e_login.persistence.Loja.loja.Loja;

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