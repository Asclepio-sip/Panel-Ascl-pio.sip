package projeto_base_de_telas_e_login.domain.repository;

import org.springframework.security.core.userdetails.UserDetails;
import projeto_base_de_telas_e_login.domain.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPorta {

    boolean existsByUsername(String username);

    User save(User user);

    User findById(UUID id);

    List<User> findAll();

    Optional<UserDetails> findByLogin(String login);

}
