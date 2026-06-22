package Asclepio.Usuario.User;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaRepository;
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
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final EmpresaRepository empresaRepository;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            PermissionRepository permissionRepository,
            EmpresaRepository empresaRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.empresaRepository = empresaRepository;
    }

    public User createUser(RegisterDTO dto) {

        validarCriacao(dto);

        if (userRepository.findByUsername(dto.login().trim()).isPresent()) {
            throw new BusinessException("Usuário já existe");
        }

        Role role = roleRepository.findById(dto.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));


        User user = new User();

        user.setUsername(dto.login().trim());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.Email());
        user.setRole(role);
        user.setAtivo(true);
        user.setEmpresa(empresa);


        return userRepository.save(user);
    }

    public Page<ResponseListaDeUserDTO> lista(UserFiltroDTO filtro, Pageable pageable) {

        Specification<User> specification = UserSpecification.filtrar(filtro);

        return userRepository
                .findAll(specification, pageable)
                .map(ResponseListaDeUserDTO::fromEntity);
    }

    public User findById(UUID id) {

        if (id == null) {
            throw new BusinessException("ID do usuário é obrigatório");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public void updateUser(UUID id, UpdateUserDTO dto) {

        if (dto == null) {
            throw new BusinessException("Dados do usuário são obrigatórios");
        }

        User user = findById(id);

        if (dto.login() != null && !dto.login().isBlank()) {

            String loginTratado = dto.login().trim();

            userRepository.findByUsername(loginTratado)
                    .ifPresent(usuarioExistente -> {
                        if (!usuarioExistente.getId().equals(id)) {
                            throw new BusinessException("Já existe outro usuário com esse login");
                        }
                    });

            user.setUsername(loginTratado);
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.roleId() != null) {
            Role role = roleRepository.findById(dto.roleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));

            user.setRole(role);
        }

        if (dto.permissionIds() != null) {
            List<Permission> permissionsExtras =
                    permissionRepository.findAllById(dto.permissionIds());

            if (permissionsExtras.size() != dto.permissionIds().size()) {
                throw new ResourceNotFoundException("Uma ou mais permissões não foram encontradas");
            }

            user.setPermissionsExtras(permissionsExtras);
        }

        userRepository.save(user);
    }

    private void validarCriacao(RegisterDTO dto) {

        if (dto == null) {
            throw new BusinessException("Dados do usuário são obrigatórios");
        }

        if (dto.login() == null || dto.login().isBlank()) {
            throw new BusinessException("Login é obrigatório");
        }

        if (dto.password() == null || dto.password().isBlank()) {
            throw new BusinessException("Senha é obrigatória");
        }

        if (dto.roleId() == null) {
            throw new BusinessException("Cargo é obrigatório");
        }
    }
}