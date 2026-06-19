package Asclepio.Loja.LojaBairro.dto;

import java.math.BigDecimal;

public record LojaBairroFiltroDTO(

        Long id,

        Long lojaId,

        String nomeLoja,

        Long bairroId,

        String nomeBairro,

        BigDecimal valorFreteMin,

        BigDecimal valorFreteMax

) {
}