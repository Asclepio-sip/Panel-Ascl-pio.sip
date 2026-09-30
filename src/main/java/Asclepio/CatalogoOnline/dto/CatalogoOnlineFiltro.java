package Asclepio.CatalogoOnline.dto;

public record CatalogoOnlineFiltro(
        String nomeProduto,
        Long categoriaId,
        Boolean somentePromocao
) {
}
