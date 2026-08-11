package Asclepio.Usuario.User.dto;

import Asclepio.UserLoja.UserLoja;
import Asclepio.Usuario.User.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record ResponseListaDeUserDTO(

        UUID id,
        String username,
        String email,
        String nome,
        boolean ativo,
        int totalLojas,
        int totalPermissoes

) {

    public static ResponseListaDeUserDTO fromEntity(
            User user,
            Long empresaId
    ) {

        Set<String> permissoes = new HashSet<>();

        int totalLojas = 0;

        if (user.getUserLojas() != null) {

            for (UserLoja userLoja : user.getUserLojas()) {

                if (userLoja.getLoja() == null) {
                    continue;
                }

                if (userLoja.getLoja().getEmpresa() == null) {
                    continue;
                }

                // Ignora lojas de outra empresa
                if (!userLoja.getLoja()
                        .getEmpresa()
                        .getId()
                        .equals(empresaId)) {

                    continue;
                }

                totalLojas++;


                if (userLoja.getRole() != null &&
                        userLoja.getRole().getPermissions() != null) {

                    userLoja.getRole()
                            .getPermissions()
                            .forEach(permission ->
                                    permissoes.add(
                                            permission.getNome()
                                    )
                            );
                }
            }
        }

        if (user.getPermissionsExtras() != null) {

            user.getPermissionsExtras()
                    .forEach(permission ->
                            permissoes.add(
                                    permission.getNome()
                            )
                    );
        }

        return new ResponseListaDeUserDTO(

                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNome(),
                user.getAtivo(),
                totalLojas,
                permissoes.size()
        );
    }
}

