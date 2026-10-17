package Asclepio.MovimentacaoEstoque.dto;

import java.util.UUID;

/** Usuário que já fez alguma movimentação de estoque (opção do filtro do relatório). */
public record MovimentacaoUsuarioResponse(
        UUID id,
        String nome
) {
}
