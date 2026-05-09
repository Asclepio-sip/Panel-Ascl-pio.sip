package projeto_base_de_telas_e_login.dto.loja.lojabairros;

import projeto_base_de_telas_e_login.model.Loja.LojaBairro;

import java.math.BigDecimal;

public record LojaBairroResponse (
         Long lojaId,
         Long bairroId,
         BigDecimal valorFrete
){

    public static LojaBairroResponse fromDomain(LojaBairro lb) {
        return new LojaBairroResponse(
                lb.getLoja().getId(),
                lb.getBairro().getId(),
                lb.getValorFrete()
        );
    }

}