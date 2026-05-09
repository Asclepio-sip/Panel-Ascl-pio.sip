package projeto_base_de_telas_e_login.adapter.out.persistence.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import projeto_base_de_telas_e_login.domain.model.user.User;
import projeto_base_de_telas_e_login.domain.repository.UserPorta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserAdapter implements UserPorta {

    private final UserRepository userRepository;

    public UserAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<UserDetails> findByLogin(String login) {
        return userRepository.findByUsername(login)
                .map(user -> (UserDetails) user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }



    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user);
        var saved = userRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .map(UserEntity::toDomain)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserEntity::toDomain)
                .toList();
    }

}
