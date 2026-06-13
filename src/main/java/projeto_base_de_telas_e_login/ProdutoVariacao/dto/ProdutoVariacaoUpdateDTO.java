package projeto_base_de_telas_e_login.ProdutoVariacao.dto;

public record ProdutoVariacaoUpdateDTO(
        String nomeVariacao,
        String codigoBarras,
        Boolean ativo
) {
}