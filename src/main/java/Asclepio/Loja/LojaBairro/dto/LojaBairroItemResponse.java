package Asclepio.Loja.LojaBairro.dto;

import java.math.BigDecimal;

public record LojaBairroItemResponse(

        Long bairroId,
        String nomeBairro,
        BigDecimal valorFrete

) {
}