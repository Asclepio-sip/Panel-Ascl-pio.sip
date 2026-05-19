package projeto_base_de_telas_e_login.Usuario.dto;

import projeto_base_de_telas_e_login.Usuario.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
