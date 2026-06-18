package Asclepio.Categoria.dto;

import java.util.List;

public record CategoriaPageResponse(
        List<CategoriaResponse> content,
        PageInfo page
) {
    public record PageInfo(
            int size,
            int number,
            long totalElements,
            int totalPages
    ) {
    }
}