package projeto_base_de_telas_e_login.Usuario.User.dto;

import projeto_base_de_telas_e_login.Usuario.Permission.Permission;
import projeto_base_de_telas_e_login.Usuario.User.User;

import java.util.List;

public record ResponseListaDeUserDTO (
        String username,
        String email,
        boolean ativo,
        List<Permission> role
) {
    public static ResponseListaDeUserDTO fromEntity(User user){
        return  new ResponseListaDeUserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getAtivo(),
                user.getRole().getPermissions()
        );
    }

}
