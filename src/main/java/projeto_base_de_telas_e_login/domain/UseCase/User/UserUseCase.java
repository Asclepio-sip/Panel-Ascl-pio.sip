package projeto_base_de_telas_e_login.domain.UseCase.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.domain.model.user.User;
import projeto_base_de_telas_e_login.domain.model.user.UserRole;
import projeto_base_de_telas_e_login.domain.repository.UserPorta;

import java.util.List;
import java.util.UUID;

@Service
public class UserUseCase {

    private final UserPorta userPorta;
    private final PasswordEncoder passwordEncoder;

    public UserUseCase(UserPorta userPorta, PasswordEncoder passwordEncoder) {
        this.userPorta = userPorta;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String login, String password, UserRole role) {

        if (userPorta.existsByUsername(login)) {
            throw new RuntimeException("Usuário já existe");
        }

        var encrypted = passwordEncoder.encode(password);

        var user = new User(
                null,
                login,
                encrypted,
                role,
                null
        );

        return userPorta.save(user);
    }

    public List<User> findAll() {
        return userPorta.findAll();
    }

    public void updateUser(UUID id, String login, String password, String role) {

        var user = userPorta.findById(id);

        user.setUsername(login);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (role != null) {
            user.setRole(UserRole.valueOf(role));
        }

        userPorta.save(user);
    }
}

