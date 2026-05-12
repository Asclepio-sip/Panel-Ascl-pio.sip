package projeto_base_de_telas_e_login.dto.loja.Loja;

import projeto_base_de_telas_e_login.model.Loja.Enum.TipoAtendimentoLoja;

import java.math.BigDecimal;

public record CreateLojaRequest(
        String nome,
        TipoAtendimentoLoja tipoAtendimento,
        BigDecimal valorMinimoFreteGratis
) {

}