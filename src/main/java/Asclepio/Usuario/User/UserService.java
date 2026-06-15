package Asclepio.Usuario.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.Permission.PermissionRepository;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.Role.RoleRepository;
import Asclepio.Usuario.User.Repository.UserRepository;
import Asclepio.Usuario.User.Repository.UserSpecification;
import Asclepio.Usuario.User.dto.RegisterDTO;
import Asclepio.Usuario.User.dto.ResponseListaDeUserDTO;
import Asclepio.Usuario.User.dto.UpdateUserDTO;
import Asclepio.Usuario.User.dto.UserFiltroDTO;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
    }

    public User createUser(RegisterDTO dto) {

        if (userRepository.findByUsername(dto.login()).isPresent()) {

            throw new RuntimeException("Usuário já existe");
        }

        Role role = roleRepository.findById(dto.roleId()).orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        User user = new User();

        user.setUsername(dto.login());

        user.setPassword(passwordEncoder.encode(dto.password()));

        user.setRole(role);

        user.setAtivo(true);

        return userRepository.save(user);
    }

    public Page<ResponseListaDeUserDTO> lista(UserFiltroDTO filtro, Pageable pageable) {

        Specification<User> specification = UserSpecification.filtrar(filtro);

        return userRepository
                .findAll(specification, pageable)
                .map(ResponseListaDeUserDTO::fromEntity);
    }

    public User findById(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void updateUser(UUID id, UpdateUserDTO dto) {
        User user = findById(id);

        user.setUsername(dto.login());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.roleId() != null) {
            Role role = roleRepository.findById(dto.roleId())
                    .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

            user.setRole(role);
        }

        if (dto.permissionIds() != null) {
            List<Permission> permissionsExtras =
                    permissionRepository.findAllById(dto.permissionIds());

            user.setPermissionsExtras(permissionsExtras);
        }


        userRepository.save(user);
    }
}