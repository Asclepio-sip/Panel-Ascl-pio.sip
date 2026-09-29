package Asclepio.Usuario.User.dto;

import Asclepio.UserLoja.UserLoja;

import java.util.UUID;

public record UserLojaAcessoDTO(

        Long lojaId,

        String lojaNome,

        UUID roleId,

        String roleNome

) {

    public static UserLojaAcessoDTO fromEntity(UserLoja userLoja) {
        return new UserLojaAcessoDTO(
                userLoja.getLoja().getId(),
                userLoja.getLoja().getNomeLoja(),
                userLoja.getRole().getId(),
                userLoja.getRole().getNome()
        );
    }
}
