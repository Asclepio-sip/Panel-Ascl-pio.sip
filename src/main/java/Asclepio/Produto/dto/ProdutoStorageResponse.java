package Asclepio.Produto.dto;

import java.time.LocalDateTime;

public record ProdutoStorageResponse(

        Long id,
        String nome,
        String descricao,
        String marca,
        String imagemUrl,
        Long categoriaId,
        String categoriaNome,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        Integer quantidadeVariacoes

) {
}