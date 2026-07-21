package Asclepio.config.bootstrap;

import Asclepio.Empresa.Empresa;
import Asclepio.Empresa.EmpresaRepository;
import Asclepio.Usuario.Role.ServiceRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.Permission.PermissionRepository;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.Role.RoleRepository;
import Asclepio.Usuario.User.User;
import Asclepio.Usuario.User.Repository.UserRepository;

import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmpresaRepository empresaRepository;
    private final ServiceRole serviceRole;


    public DataInitializer(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmpresaRepository empresaRepository,
            ServiceRole serviceRole
    ) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.empresaRepository = empresaRepository;
        this.serviceRole = serviceRole;
    }

    @Override
    public void run(String... args) {
        criarPermissoes();
        criarAdmin();
    }

    private void criarPermissoes() {

        criarPermission("VerProduto", "Ver produtos");
        criarPermission("CriarProduto", "Criar produtos");
        criarPermission("EditarProduto", "Editar produtos");
        criarPermission("ExcluirProduto", "Excluir produtos");

        criarPermission("VerEstoque", "Ver estoque");
        criarPermission("CriarEstoque", "Criar estoque");
        criarPermission("EditarEstoque", "Editar estoque");
        criarPermission("ExcluirEstoque", "Excluir estoque");
        criarPermission("PromocaoEstoque", "Criar promoção no estoque");

        criarPermission("VerLoja", "Ver loja");
        criarPermission("CriarLoja", "Criar loja");
        criarPermission("EditarLoja", "Editar loja");
        criarPermission("ExcluirLoja", "Excluir loja");

        criarPermission("VerBairro", "Ver bairro");
        criarPermission("CriarBairro", "Criar bairro");
        criarPermission("EditarBairro", "Editar bairro");
        criarPermission("ExcluirBairro", "Excluir bairro");

        criarPermission("VerLojaBairro", "Ver loja bairro");
        criarPermission("CriarLojaBairro", "Criar loja bairro");
        criarPermission("EditarLojaBairro", "Editar loja bairro");
        criarPermission("ExcluirLojaBairro", "Excluir loja bairro");

        criarPermission("VerPedido", "Ver pedido");
        criarPermission("CriarPedido", "Criar pedido");
        criarPermission("EditarPedido", "Editar pedido");
        criarPermission("ExcluirPedido", "Excluir pedido");

        criarPermission("VerUsuario", "Ver usuário");
        criarPermission("CriarUsuario", "Criar usuário");
        criarPermission("EditarUsuario", "Editar usuário");
        criarPermission("ExcluirUsuario", "Excluir usuário");

        criarPermission("VerCategoria", "Ver categoria");
        criarPermission("CriarCategoria", "Criar categoria");
        criarPermission("EditarCategoria", "Editar categoria");
        criarPermission("ExcluirCategoria", "Excluir categoria");


        criarPermission("VerUser", "Ver User");
        criarPermission("CriarUser", "Criar User");
        criarPermission("EditarUser", "Editar User");
        criarPermission("ExcluirUser", "Excluir User");

        criarPermission("VerProdutoVariacao", "Ver variações de produto");
        criarPermission("CriarProdutoVariacao", "Criar variação de produto");
        criarPermission("EditarProdutoVariacao", "Editar variação de produto");
        criarPermission("ExcluirProdutoVariacao", "Excluir variação de produto");


        criarPermission("VerEmpresa", "Ver empresa");
        criarPermission("CriarEmpresa", "Criar empresa");
        criarPermission("EditarEmpresa", "Editar empresa");
        criarPermission("ExcluirEmpresa", "Excluir empresa");

        criarPermission("VerPermissoes", "Ver permissões");

        criarPermission("VerRole", "Ver Role");

    }

    private void criarPermission(String nome, String descricao) {
        if (permissionRepository.findByNome(nome).isEmpty()) {
            permissionRepository.save(new Permission(null, nome, descricao));
        }
    }



    private void criarAdmin() {


        if(userRepository.findByUsername("suporte1").isPresent()){
            return;
        }
        Empresa empresa = new Empresa();
        empresa.setNome("Empresa Suporte");
        empresa.setCnpj("00000000000000");
        empresa.setAtiva(true);

        empresa = empresaRepository.save(empresa);

        serviceRole.criarRolesPadrao(empresa);

        Role role = serviceRole.buscarSuperAdministrador(empresa);
        User user = new User();

        user.setUsername("suporte1");

        user.setPassword(
                passwordEncoder.encode("123")
        );

        user.setEmail("suporte@email.com");
        user.setAtivo(true);
        user.setEmpresa(empresa);
        user.setRole(role);
        userRepository.save(user);


        System.out.println(
                "Usuário administrador criado!"
        );

    }
}