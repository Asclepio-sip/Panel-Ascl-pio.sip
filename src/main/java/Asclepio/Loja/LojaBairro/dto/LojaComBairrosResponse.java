package Asclepio.Loja.LojaBairro.dto;

import java.util.List;

public record LojaComBairrosResponse(

        Long lojaId,
        String nomeLoja,
        List<LojaBairroItemResponse> bairros

) {
}