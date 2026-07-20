package Asclepio.ProdutoVariacao.dto;

import java.util.List;

public record ProdutoVariacaoPageResponse(
        List<ProdutoVariacaoResponseDTO> content,
        int totalPages,
        long totalElements,
        int size,
        int number,
        boolean first,
        boolean last,
        int numberOfElements,
        boolean empty
) {
}