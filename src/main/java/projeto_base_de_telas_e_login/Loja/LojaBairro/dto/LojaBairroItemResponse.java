package projeto_base_de_telas_e_login.Loja.LojaBairro.dto;

import java.math.BigDecimal;

public record LojaBairroItemResponse(

        Long bairroId,
        String nomeBairro,
        BigDecimal valorFrete

) {
}