package projeto_base_de_telas_e_login.adapter.in.web.dto.User;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(@NotBlank String login, String password, String role  ) {

}
