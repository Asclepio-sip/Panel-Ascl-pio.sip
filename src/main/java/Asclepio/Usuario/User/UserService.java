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

import java.util.ArrayList;
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

    @Transactional
    public User createUser(RegisterDTO dto) {

        validarCriacao(dto);

        Long empresaId = empresaContext.getEmpresaId();

        // ==========================================
        // VALIDA E BUSCA TODAS AS LOJAS + ROLES
        // ==========================================

        List<UserLoja> vinculos = new ArrayList<>();

        for (UsuarioLojaDTO item : dto.lojas()) {

            Loja loja = lojaRepository
                    .findByIdAndEmpresa_Id(item.lojaId(), empresaId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Loja não encontrada: " + item.lojaId())
                    );

            Role role = roleRepository
                    .findByIdAndEmpresa_Id(item.roleId(), empresaId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Cargo não encontrado: " + item.roleId())
                    );

            UserLoja userLoja = new UserLoja();
            userLoja.setLoja(loja);
            userLoja.setRole(role);

            vinculos.add(userLoja);
        }

        // ==========================================
        // GERA LOGIN A PARTIR DO NOME DA EMPRESA
        // ==========================================

        Empresa empresa = empresaService.buscarPorId(empresaId); // ajuste se o nome do método for diferente

        String login = gerarLogin(
                empresa.getNome(),
                dto.nome()
        );

        if (userRepository.findByUsername(login).isPresent()) {
            throw new BusinessException(
                    "Já existe um usuário com o login: " + login
            );
        }

        if (userRepository.findByEmail(dto.email().trim()).isPresent()) {
            throw new BusinessException(
                    "E-mail já cadastrado."
            );
        }
        User user = new User();

        user.setUsername(login);           // continua sendo empresa@nome
        user.setNome(dto.nome().trim());   // NOVO: guarda o nome de exibição
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email().trim());
        user.setAtivo(true);

        user = userRepository.save(user);

        // ==========================================
        // SALVA TODOS OS VÍNCULOS USER + LOJA + ROLE
        // ==========================================

        for (UserLoja userLoja : vinculos) {
            userLoja.setUser(user);
            userLojaRepository.save(userLoja);
        }

        return user;
    }

    private String gerarLogin(String nomeEmpresa, String nomeUsuario) {

        String empresa = normalizarLogin(nomeEmpresa);
        String usuario = normalizarLogin(nomeUsuario);

        return empresa + "@" + usuario;
    }


    private String normalizarLogin(String valor) {

        return java.text.Normalizer
                .normalize(
                        valor.trim().toLowerCase(),
                        java.text.Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9]", "");
    }

    private void validarCriacao(RegisterDTO dto) {

        if (dto == null) {
            throw new BusinessException("Dados do usuário são obrigatórios.");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new BusinessException("Nome do usuário é obrigatório.");
        }

        if (dto.password() == null || dto.password().isBlank()) {
            throw new BusinessException("Senha é obrigatória.");
        }

        if (dto.email() == null || dto.email().isBlank()) {
            throw new BusinessException("E-mail é obrigatório.");
        }

        if (dto.lojas() == null || dto.lojas().isEmpty()) {
            throw new BusinessException("É necessário informar ao menos uma loja.");
        }

        for (UsuarioLojaDTO item : dto.lojas()) {
            if (item.lojaId() == null) {
                throw new BusinessException("Loja é obrigatória.");
            }
            if (item.roleId() == null) {
                throw new BusinessException("Cargo é obrigatório.");
            }
        }
    }
    public Page<ResponseListaDeUserDTO> lista(UserFiltroDTO filtro, Pageable pageable) {

        Long empresaId = empresaContext.getEmpresaId();

        Specification<User> specification = UserSpecification.filtrar(filtro, empresaId);

        return userRepository.findAll(specification, pageable).map(user -> ResponseListaDeUserDTO.fromEntity(user, empresaId));
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

    @Transactional
    public User criarConta(RequestCriarContaDTO dto) {

        validationService.validarCriacaoConta(dto);
        validationService.validarEmail(dto.email());

        Empresa empresa = empresaService.criarEmpresaCadastro(
                dto.nomeEmpresa().trim()
        );

        serviceRole.criarRolesPadrao(empresa);

        Role role = serviceRole.buscarSuperAdministrador(empresa);

        User user = new User();

        user.setUsername(dto.email().trim().toLowerCase()); // login = e-mail
        user.setNome(dto.nome().trim());                     // nome de exibição
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

    private boolean ehGerente(List<UserLoja> vinculos) {
        return vinculos.stream()
                .anyMatch(ul -> ul.getRole().getNome().equalsIgnoreCase("Gerente"));
    }

    public LoginResponseDTO escolherLoja(User user, Long lojaId) {

        List<UserLoja> vinculos = userLojaRepository.findAllByUser(user);
        boolean ehGerente = ehGerente(vinculos);

        if (lojaId == null) {

            if (!ehGerente) {
                throw new BusinessException("Apenas o Gerente pode visualizar todas as lojas.");
            }

            UserLoja qualquerVinculo = vinculos.get(0);
            Long empresaId = qualquerVinculo.getLoja().getEmpresa().getId();

            List<String> permissoes = qualquerVinculo.getRole().getPermissions()
                    .stream().map(Permission::getNome).toList();

            String token = tokenService.generateToken(user, empresaId, null, permissoes, true);

            return new LoginResponseDTO(token, false, List.of(), null);
        }

        UserLoja userLoja = userLojaRepository.findByUser_IdAndLoja_Id(user.getId(), lojaId)
                .orElseThrow(() -> new BusinessException("Usuário não possui acesso a esta loja."));

        List<String> permissoes = userLoja.getRole().getPermissions().stream().map(Permission::getNome).toList();

        String token = tokenService.generateToken(
                user,
                userLoja.getLoja().getEmpresa().getId(),
                userLoja.getLoja().getId(),
                permissoes,
                ehGerente
        );

        return new LoginResponseDTO(token, false, List.of(), userLoja.getLoja().getNomeLoja());
    }

    public LoginResponseDTO login(AuthenticationDTO data) {

        var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(authToken);
        var usuarioAutenticado = (UsuarioAutenticado) auth.getPrincipal();

        // ===== MIGRAÇÃO GRADUAL DE SENHA =====
        User user = usuarioAutenticado.getUser();

        if (passwordEncoder.upgradeEncoding(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(data.password()));
            userRepository.save(user);
        }
        // ======================================

        List<UserLoja> lojas = userLojaRepository.findAllByUser(user);
        boolean ehGerente = ehGerente(lojas);

        if (lojas.size() == 1 && !ehGerente) {
            UserLoja userLoja = lojas.get(0);
            List<String> permissoes = userLoja.getRole().getPermissions().stream()
                    .map(Permission::getNome).toList();

            String token = tokenService.generateToken(
                    usuarioAutenticado.getUser(),
                    userLoja.getLoja().getEmpresa().getId(),
                    userLoja.getLoja().getId(),
                    permissoes,
                    false
            );

            storageWakeUpService.acordarStorage();

            return new LoginResponseDTO(token, false, List.of(), userLoja.getLoja().getNomeLoja());
        }

        // ===== NOVO: gera token TEMP em vez de token null =====
        List<LojaLoginDTO> resposta = lojas.stream()
                .map(userLoja -> new LojaLoginDTO(userLoja.getLoja().getId(), userLoja.getLoja().getNomeLoja()))
                .toList();

        String tempToken = tokenService.generateTempToken(usuarioAutenticado.getUser());

        return new LoginResponseDTO(tempToken, true, resposta, null);
    }
}