package projeto_base_de_telas_e_login.Estoque.dto;

public record EstoqueFiltro(
        Long lojaId,
        String nomeLoja,
        String nomeProduto,
        String nomeVariacao,
        Long categoriaId,
        String nomeCategoria,
        Boolean semEstoque
) {
}