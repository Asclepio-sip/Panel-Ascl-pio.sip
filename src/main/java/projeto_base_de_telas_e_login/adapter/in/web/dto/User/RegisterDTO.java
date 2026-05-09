package projeto_base_de_telas_e_login.adapter.in.web.dto.User;

import projeto_base_de_telas_e_login.domain.model.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}
