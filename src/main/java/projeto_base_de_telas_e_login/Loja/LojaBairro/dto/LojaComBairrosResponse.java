package projeto_base_de_telas_e_login.Loja.LojaBairro.dto;

import java.util.List;

public record LojaComBairrosResponse(

        Long lojaId,
        String nomeLoja,
        List<LojaBairroItemResponse> bairros

) {
}