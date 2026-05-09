package projeto_base_de_telas_e_login.adapter.in.web.dto.Estoque;

import projeto_base_de_telas_e_login.domain.model.estoque.Estoque;

import java.math.BigDecimal;

public record EstoqueListaDto(
        Long id,
        Long lojaId,
        Long produtoId,
        Integer quantidade,
        BigDecimal precoVenda
) {
    public static EstoqueListaDto fromDomain(Estoque estoque) {
        return new EstoqueListaDto(
                estoque.getId(),
                estoque.getLoja().getId(),
                estoque.getProduto().getId(),
                estoque.getQuantidade(),
                estoque.getPrecoVenda().getValor()
        );
    }
}