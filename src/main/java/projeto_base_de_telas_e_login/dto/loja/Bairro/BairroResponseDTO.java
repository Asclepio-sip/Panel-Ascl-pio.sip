package projeto_base_de_telas_e_login.dto.loja.Bairro;

import projeto_base_de_telas_e_login.model.Loja.Bairro;

public record BairroResponseDTO(
        Long id,
        String nome
) {
    public static BairroResponseDTO fromDomain(Bairro bairro) {
        return new BairroResponseDTO(bairro.getId(), bairro.getNome());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}