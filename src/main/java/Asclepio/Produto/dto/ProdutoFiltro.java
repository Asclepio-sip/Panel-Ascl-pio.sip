package Asclepio.Produto.dto;



public record ProdutoFiltro(
        Long id,
        String nome,
        String marca,
        String variacao,
        String codigoBarras,
        Long categoriaId,
        String nomeCategoria) {
}