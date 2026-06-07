package projeto_base_de_telas_e_login.Usuario.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projeto_base_de_telas_e_login.Usuario.Role.Role;
import projeto_base_de_telas_e_login.Usuario.Role.RoleRepository;
import projeto_base_de_telas_e_login.Usuario.User.dto.ResponseListaDeUserDTO;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String login, String password, UUID roleId) {

        if (userRepository.findByUsername(login).isPresent()) {

            throw new RuntimeException("Usuário já existe");
        }

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        User user = new User();

        user.setUsername(login);

        user.setPassword(passwordEncoder.encode(password));

        user.setRole(role);

        user.setAtivo(true);

        return userRepository.save(user);
    }

    public List<ResponseListaDeUserDTO> lista() {
        return userRepository.findAll().stream().map(ResponseListaDeUserDTO::fromEntity).toList();
    }

    public User findById(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void updateUser(UUID id, String login, String password, UUID roleId) {

        User user = findById(id);

        user.setUsername(login);

        if (password != null && !password.isBlank()) {

            user.setPassword(passwordEncoder.encode(password));
        }

        if (roleId != null) {

            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

            user.setRole(role);
        }

        userRepository.save(user);
    }
}