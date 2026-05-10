package projeto_base_de_telas_e_login.dto.loja.lojabairros;

import projeto_base_de_telas_e_login.persistence.Loja.LojaBairro.LojaBairro;

import java.math.BigDecimal;

public record LojaBairroResponse(

        Long id,
        Long lojaId,
        Long bairroId,
        BigDecimal valorFrete

) {

    public static LojaBairroResponse fromEntity(
            LojaBairro lb
    ) {

        return new LojaBairroResponse(
                lb.getId(),
                lb.getLoja().getId(),
                lb.getBairro().getId(),
                lb.getValorFrete()
        );
    }
}