package projeto_base_de_telas_e_login.dto.User;

import projeto_base_de_telas_e_login.model.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
