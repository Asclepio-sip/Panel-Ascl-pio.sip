package Asclepio.Usuario.User.dto;

import Asclepio.Usuario.User.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record ResponseListaDeUserDTO(
        UUID id,
        String username,
        String email,
        boolean ativo,
        String role,
        int totalPermissoes
) {

    public static ResponseListaDeUserDTO fromEntity(User user) {

        Set<String> permissoes = new HashSet<>();

        if (user.getRole() != null) {
            user.getRole()
                    .getPermissions()
                    .forEach(p -> permissoes.add(p.getNome()));
        }

        if (user.getPermissionsExtras() != null) {
            user.getPermissionsExtras()
                    .forEach(p -> permissoes.add(p.getNome()));
        }

        return new ResponseListaDeUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAtivo(),
                user.getRole() != null ? user.getRole().getNome() : null,
                permissoes.size()
        );
    }
}