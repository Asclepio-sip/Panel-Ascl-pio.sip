package projeto_base_de_telas_e_login.dto.loja.Bairro;

import projeto_base_de_telas_e_login.persistence.Loja.Bairro.Bairro;

public record BairroResponseDTO(
        Long id,
        String nome
) {

    public static BairroResponseDTO fromEntity(
            Bairro bairro
    ) {

        return new BairroResponseDTO(
                bairro.getId(),
                bairro.getNome()
        );
    }
}