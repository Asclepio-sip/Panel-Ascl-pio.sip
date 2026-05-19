package projeto_base_de_telas_e_login.Produto.dto;

public record ProdutoUpdateDto(
        String name,
        String variacao,
        String imagemBase64,
        String categoriaNome
) {}