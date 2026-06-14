package projeto_base_de_telas_e_login.config.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import projeto_base_de_telas_e_login.Usuario.Permission.Permission;
import projeto_base_de_telas_e_login.Usuario.Permission.PermissionRepository;
import projeto_base_de_telas_e_login.Usuario.Role.Role;
import projeto_base_de_telas_e_login.Usuario.Role.RoleRepository;
import projeto_base_de_telas_e_login.Usuario.User.User;
import projeto_base_de_telas_e_login.Usuario.User.Repository.UserRepository;

import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        criarPermissoes();
        criarRoles();
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

        criarPermission("VerPermissoes", "Ver permissões");

        criarPermission("VerRole", "Ver Role");

    }

    private void criarPermission(String nome, String descricao) {
        if (permissionRepository.findByNome(nome).isEmpty()) {
            permissionRepository.save(new Permission(null, nome, descricao));
        }
    }

    private void criarRoles() {

        Role superAdmin = roleRepository.findByNome("SuperAdministrador").orElse(new Role());

        superAdmin.setNome("SuperAdministrador");
        superAdmin.setDescricao("Administrador total");
        superAdmin.setPermissions(permissionRepository.findAll());

        roleRepository.save(superAdmin);

        Role gerente = roleRepository.findByNome("Gerente").orElse(new Role());

        gerente.setNome("Gerente");
        gerente.setDescricao("Gerente da farmácia");

        gerente.setPermissions(permissionRepository.findByNomeIn(List.of(
                "VerProduto", "CriarProduto", "EditarProduto", "ExcluirProduto",

                "VerEstoque", "CriarEstoque", "EditarEstoque", "ExcluirEstoque", "PromocaoEstoque",

                "VerPedido", "CriarPedido", "EditarPedido", "ExcluirPedido",

                "VerUsuario", "CriarUsuario", "EditarUsuario", "ExcluirUsuario",

                "VerCategoria", "CriarCategoria", "EditarCategoria", "ExcluirCategoria",

                "VerBairro", "CriarBairro", "EditarBairro", "ExcluirBairro",

                "VerLojaBairro", "CriarLojaBairro", "EditarLojaBairro", "ExcluirLojaBairro",

                "VerPermissoes",
                "VerProdutoVariacao",
                "CriarProdutoVariacao",
                "EditarProdutoVariacao",
                "ExcluirProdutoVariacao",
                "VerLoja"
        )));

        roleRepository.save(gerente);

        Role atendente = roleRepository.findByNome("Atendente").orElse(new Role());

        atendente.setNome("Atendente");
        atendente.setDescricao("Atendente da farmácia");

        atendente.setPermissions(permissionRepository.findByNomeIn(List.of(
                "VerProduto",
                "VerEstoque",
                "VerPedido",
                "CriarPedido",
                "EditarPedido",
                "VerProdutoVariacao",
                "VerCategoria"
        )));

        roleRepository.save(atendente);

        Role repositor = roleRepository.findByNome("Repositor").orElse(new Role());

        repositor.setNome("Repositor");
        repositor.setDescricao("Repositor / controle de estoque");

        repositor.setPermissions(permissionRepository.findByNomeIn(List.of(
                "VerProduto",
                "VerEstoque",
                "CriarEstoque",
                "EditarEstoque",
                "VerCategoria",
                "VerProdutoVariacao"
        )));

        roleRepository.save(repositor);

        Role caixa = roleRepository.findByNome("Caixa").orElse(new Role());

        caixa.setNome("Caixa");
        caixa.setDescricao("Operador de caixa");

        caixa.setPermissions(permissionRepository.findByNomeIn(List.of(
                "VerProduto",
                "VerEstoque",
                "VerPedido",
                "CriarPedido",
                "VerProdutoVariacao",
                "EditarPedido"
        )));

        roleRepository.save(caixa);

        Role farmaceutico = roleRepository.findByNome("Farmaceutico").orElse(new Role());

        farmaceutico.setNome("Farmaceutico");
        farmaceutico.setDescricao("Farmacêutico responsável");

        farmaceutico.setPermissions(permissionRepository.findByNomeIn(List.of(
                "VerProduto",
                "VerEstoque",
                "EditarEstoque",
                "VerPedido",
                "EditarPedido",
                "VerProdutoVariacao",
                "VerCategoria"
        )));

        roleRepository.save(farmaceutico);


        Role generico = roleRepository.findByNome("Generico")
                .orElse(new Role());

        generico.setNome("Generico");
        generico.setDescricao("Usuário sem permissões");

        generico.setPermissions(List.of());

        roleRepository.save(generico);
    }

    private void criarAdmin() {

        if (userRepository.findByUsername("suporte1").isEmpty()) {

            Role role = roleRepository.findByNome("SuperAdministrador")
                    .orElseThrow();

            User user = new User();

            user.setUsername("suporte1");

            user.setPassword(
                    passwordEncoder.encode("123")
            );

            user.setEmail("suporte@email.com");

            user.setAtivo(true);

            user.setRole(role);

            userRepository.save(user);

            System.out.println("Usuário administrador criado!");
        }
    }
}