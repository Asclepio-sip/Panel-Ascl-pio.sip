package projeto_base_de_telas_e_login.dto.Estoque;

import java.math.BigDecimal;

public record EstoqueAddDto(
        Long lojaID,
        String nomeLoja,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoVenda
) {

}