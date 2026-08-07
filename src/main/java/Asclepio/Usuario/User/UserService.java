package Asclepio.Usuario.User;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaContext;
import Asclepio.Empresa.EmpresaService;
import Asclepio.Loja.FormaPagamento.LojaFormaPagamento;
import Asclepio.Loja.FormaPagamento.LojaFormaPagamentoRepository;
import Asclepio.Loja.Loja.Loja;
import Asclepio.Loja.Loja.Repository.LojaRepository;
import Asclepio.Pedido.Enum.FormaDePagamento;
import Asclepio.UserLoja.UserLoja;
import Asclepio.UserLoja.UserLojaRepository;
import Asclepio.UserLoja.UsuarioLojaDTO;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.Permission.PermissionRepository;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.Role.RoleRepository;
import Asclepio.Usuario.StorageWakeUpService;
import Asclepio.Usuario.User.Repository.UserRepository;
import Asclepio.Usuario.User.Repository.UserSpecification;
import Asclepio.Usuario.User.dto.*;
import Asclepio.config.security.TokenService;
import Asclepio.config.security.UsuarioAutenticado;
import Asclepio.exception.BusinessException;
import Asclepio.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import Asclepio.Usuario.Role.ServiceRole;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final EmpresaService empresaService;
    private final UserValidationService validationService;
    private final ServiceRole serviceRole;
    private final EmpresaContext empresaContext;
    private final LojaRepository lojaRepository;
    private final UserLojaRepository userLojaRepository;
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;
    private final StorageWakeUpService storageWakeUpService;

    private final LojaFormaPagamentoRepository lojaFormaPagamentoRepository;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionRepository, EmpresaService empresaService, UserValidationService validationService, ServiceRole serviceRole, EmpresaContext empresaContext, LojaRepository lojaRepository, UserLojaRepository userLojaRepository, TokenService tokenService, AuthenticationManager authenticationManager, StorageWakeUpService storageWakeUpService,LojaFormaPagamentoRepository lojaFormaPagamentoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
        this.empresaService = empresaService;
        this.validationService = validationService;
        this.serviceRole = serviceRole;
        this.empresaContext = empresaContext;
        this.lojaRepository = lojaRepository;
        this.userLojaRepository = userLojaRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.storageWakeUpService = storageWakeUpService;
        this.lojaFormaPagamentoRepository = lojaFormaPagamentoRepository;
    }

    public User createUser(RegisterDTO dto) {

        validarCriacao(dto);

        Long empresaId = empresaContext.getEmpresaId();

        User usuarioExistente = userRepository.findByUsername(dto.login().trim()).orElse(null);

        if (usuarioExistente != null && userLojaRepository.existsByUser_IdAndLoja_Empresa_Id(usuarioExistente.getId(), empresaId)) {

            throw new BusinessException("Usuário já existe.");
        }

        User user;

        if (usuarioExistente == null) {

            user = new User();
            user.setUsername(dto.login().trim());
            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setEmail(dto.email().trim());
            user.setAtivo(true);

            user = userRepository.save(user);

        } else {

            user = usuarioExistente;
        }

        for (UsuarioLojaDTO dtoLoja : dto.lojas()) {

            Loja loja = lojaRepository.findByIdAndEmpresa_Id(dtoLoja.lojaId(), empresaId).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada."));

            Role role = roleRepository.findByIdAndEmpresa_Id(dtoLoja.roleId(), empresaId).orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado."));

            boolean jaPossuiAcesso = userLojaRepository.findByUser_IdAndLoja_Id(user.getId(), loja.getId()).isPresent();

            if (jaPossuiAcesso) {
                continue;
            }

            UserLoja userLoja = new UserLoja();
            userLoja.setUser(user);
            userLoja.setLoja(loja);
            userLoja.setRole(role);

            userLojaRepository.save(userLoja);
        }

        return user;
    }

    public Page<ResponseListaDeUserDTO> lista(UserFiltroDTO filtro, Pageable pageable) {

        Long empresaId = empresaContext.getEmpresaId();

        Specification<User> specification = UserSpecification.filtrar(filtro, empresaId);

        return userRepository.findAll(specification, pageable).map(ResponseListaDeUserDTO::fromEntity);
    }

    public User findById(UUID id) {

        if (id == null) {
            throw new BusinessException("ID obrigatório");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Long empresaId = empresaContext.getEmpresaId();

        boolean pertenceEmpresa = userLojaRepository.existsByUser_IdAndLoja_Empresa_Id(id, empresaId);

        if (!pertenceEmpresa) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }

        return user;
    }

    public void updateUser(UUID id, UpdateUserDTO dto) {

        if (dto == null) {
            throw new BusinessException("Dados do usuário são obrigatórios");
        }

        User user = findById(id);

        if (dto.login() != null && !dto.login().isBlank()) {

            String loginTratado = dto.login().trim();

            userRepository.findByUsername(loginTratado).ifPresent(usuarioExistente -> {
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
            Long empresaId = empresaContext.getEmpresaId();

            Role role = roleRepository.findByIdAndEmpresa_Id(dto.roleId(), empresaId).orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
        }

        if (dto.permissionIds() != null) {
            List<Permission> permissionsExtras = permissionRepository.findAllById(dto.permissionIds());

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


    }
    @Transactional
    public User criarConta(RequestCriarContaDTO dto) {

        validationService.validarCriacaoConta(dto);
        validationService.validarLogin(dto.login());
        validationService.validarEmail(dto.email());

        Empresa empresa = empresaService.criarEmpresaCadastro(
                dto.nomeEmpresa().trim()
        );


        serviceRole.criarRolesPadrao(empresa);

        Role role = serviceRole.buscarSuperAdministrador(empresa);

        User user = new User();

        user.setUsername(dto.login().trim());
        user.setEmail(dto.email().trim());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setAtivo(true);

        user = userRepository.save(user);

        Loja loja = new Loja();

        loja.setNomeLoja(dto.nomeLoja().trim());
        loja.setEmpresa(empresa);

        loja.setCep(dto.cep().trim());

        if (dto.cnpj() != null && !dto.cnpj().isBlank()) {
            loja.setCnpj(dto.cnpj().trim());
        }

        loja.setTelefone(dto.telefone().trim());

        if (dto.textoDescricao() != null) {
            loja.setTextoDescricao(dto.textoDescricao().trim());
        }

        loja.setTipoAtendimento(dto.tipoAtendimento());

        loja = lojaRepository.save(loja);


        // ==========================================
        // 5. VINCULA USUÁRIO À LOJA
        // ==========================================

        UserLoja userLoja = new UserLoja();

        userLoja.setUser(user);
        userLoja.setLoja(loja);
        userLoja.setRole(role);

        userLojaRepository.save(userLoja);


        for (FormaDePagamento formaPagamento : FormaDePagamento.values()) {

            LojaFormaPagamento forma = new LojaFormaPagamento();

            forma.setLoja(loja);
            forma.setFormaPagamento(formaPagamento);

            boolean ativa = dto.formasPagamento()
                    .contains(formaPagamento);

            forma.setAtivo(ativa);

            lojaFormaPagamentoRepository.save(forma);
        }



        return user;
    }

    public LoginResponseDTO escolherLoja(User user, Long lojaId) {

        UserLoja userLoja = userLojaRepository.findByUser_IdAndLoja_Id(user.getId(), lojaId).orElseThrow(() -> new BusinessException("Usuário não possui acesso a esta loja."));

        List<String> permissoes = userLoja.getRole().getPermissions().stream().map(Permission::getNome).toList();

        String token = tokenService.generateToken(user, userLoja.getLoja().getEmpresa().getId(), userLoja.getLoja().getId(), permissoes);

        return new LoginResponseDTO(token, false, List.of());
    }


    public LoginResponseDTO login(AuthenticationDTO data) {

        var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        var auth = authenticationManager.authenticate(authToken);

        var usuarioAutenticado = (UsuarioAutenticado) auth.getPrincipal();

        List<UserLoja> lojas = userLojaRepository.findAllByUser(usuarioAutenticado.getUser());

        if (lojas.isEmpty()) {
            throw new BusinessException("Usuário não possui acesso a nenhuma loja.");
        }

        if (lojas.size() == 1) {

            UserLoja userLoja = lojas.get(0);

            List<String> permissoes = userLoja.getRole().getPermissions().stream().map(Permission::getNome).toList();

            String token = tokenService.generateToken(usuarioAutenticado.getUser(), userLoja.getLoja().getEmpresa().getId(), userLoja.getLoja().getId(), permissoes);

            storageWakeUpService.acordarStorage();

            return new LoginResponseDTO(token, false, List.of());
        }

        List<LojaLoginDTO> resposta = lojas.stream().map(userLoja -> new LojaLoginDTO(userLoja.getLoja().getId(), userLoja.getLoja().getNomeLoja())).toList();

        return new LoginResponseDTO(null, true, resposta);
    }
}