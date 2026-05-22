package projeto_base_de_telas_e_login.Estoque.dto;

import java.math.BigDecimal;

public record AtulizarResquet(

        Long lojaID,

        String nomeLoja,

        Long produtoId,

        String nomeProduto,

        Integer quantidade,

        BigDecimal precoVenda) {
}
