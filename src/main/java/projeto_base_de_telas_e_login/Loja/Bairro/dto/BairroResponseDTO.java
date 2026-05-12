package projeto_base_de_telas_e_login.Loja.Bairro.dto;

import projeto_base_de_telas_e_login.Loja.Bairro.Bairro;

public record BairroResponseDTO(
        Long id,
        String nome
){

    public static BairroResponseDTO fromEntity(Bairro bairro) {

        return new BairroResponseDTO(bairro.getId(), bairro.getNome());
    }
}