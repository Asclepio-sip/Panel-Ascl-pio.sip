package Asclepio.Loja.Bairro.dto;

import Asclepio.Loja.Bairro.Bairro;

public record BairroResponseDTO(
        Long id,
        String nome
){

    public static BairroResponseDTO fromEntity(Bairro bairro) {

        return new BairroResponseDTO(bairro.getId(), bairro.getNome());
    }
}