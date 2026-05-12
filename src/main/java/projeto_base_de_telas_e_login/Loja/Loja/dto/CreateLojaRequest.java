package projeto_base_de_telas_e_login.Loja.Loja.dto;

import projeto_base_de_telas_e_login.Loja.Bairro.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;

public record CreateLojaRequest(
        String nome,
        TipoAtendimentoLoja tipoAtendimento,
        BigDecimal valorMinimoFreteGratis
) {

}