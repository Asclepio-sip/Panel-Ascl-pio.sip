package projeto_base_de_telas_e_login.config.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import projeto_base_de_telas_e_login.Usuario.Permission.Permission;
import projeto_base_de_telas_e_login.Usuario.Role.Role;
import projeto_base_de_telas_e_login.Usuario.User.User;
import projeto_base_de_telas_e_login.Usuario.Permission.PermissionRepository;
import projeto_base_de_telas_e_login.Usuario.Role.RoleRepository;
import projeto_base_de_telas_e_login.Usuario.User.UserRepository;

import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        criarPermissions();
        criarRoles();
        criarAdmin();
    }

    private void criarPermissions() {

        //Produto permissions     @PreAuthorize("hasAuthority('CATEGORIA_CREATE')")
        criarPermission("PRODUTO_READ", "Ver produtos");
        criarPermission("PRODUTO_CREATE", "Criar produtos");
        criarPermission("PRODUTO_UPDATE", "Editar produtos");
        criarPermission("PRODUTO_DELETE", "Excluir produtos");

        //Estoque permissions
        criarPermission("ESTOQUE_READ", "Ver estoque");
        criarPermission("ESTOQUE_CREATE", "Criar estoque");
        criarPermission("ESTOQUE_UPDATE", "Editar estoque");
        criarPermission("ESTOQUE_DELETE", "Excluir estoque");
        criarPermission("ESTOQUE_PROMO", "Promocao do produto que estive em estoque");


        //LOJA permissions
        criarPermission("LOJA_READ", "Ver loja");
        criarPermission("LOJA_CREATE", "Criar Loja");
        criarPermission("LOJA_UPDATE", "Editar Loja");
        criarPermission("LOJA_DELETE", "Excluir Bairro");

        //BAIRRO permissons
        criarPermission("BAIRRO_READ", "Ver bairro");
        criarPermission("BAIRRO_CREATE", "Criar bairro");
        criarPermission("BAIRRO_UPDATE", "Editar Bairro");
        criarPermission("BAIRRO_DELETE", "ExcluirBairro");

        //LOJA_BAIRRO permissons
        criarPermission("LOJA_BAIRRO_READ", "Ver bairro_loja");
        criarPermission("LOJA_BAIRRO_CREATE", "Criar bairro_loja");
        criarPermission("LOJA_BAIRRO_UPDATE", "Editar Bairro_loja");
        criarPermission("LOJA_BAIRRO_DELETE", "Excluir Bairro_loja");

        //PEDIDO permissons
        criarPermission("PEDIDO_READ", "Ver PEDIDO");
        criarPermission("PEDIDO_CREATE", "Criar PEDIDO");
        criarPermission("PEDIDO_UPDATE", "Editar PEDIDO");
        criarPermission("PEDIDO_DELETE", "Excluir PEDIDO");

        //Usuario permissons
        criarPermission("USUARIO_READ", "Ver USUARIO");
        criarPermission("USUARIO_CREATE", "Criar USUARIO");
        criarPermission("USUARIO_UPDATE", "Editar USUARIO");
        criarPermission("USUARIO_DELETE", "Excluir USUARIO");

        //CATEGORIA permissons
        criarPermission("CATEGORIA_READ", "Ver CATEGORIA");
        criarPermission("CATEGORIA_CREATE", "Criar CATEGORIA");
        criarPermission("CATEGORIA_UPDATE", "Editar CATEGORIA");
        criarPermission("CATEGORIA_DELETE", "Excluir CATEGORIA");

        //permissons permissons
        criarPermission("PERMISSIONS_READ", "ver Permissions");


    }

    private void criarPermission(String nome, String descricao) {

        if (permissionRepository.findByNome(nome).isEmpty()) {

            permissionRepository.save(new Permission(null, nome, descricao));
        }
    }

    private void criarRoles() {

        // SUPER ADMIN
        Role superAdmin = roleRepository.findByNome("SUPER_ADMIN").orElse(new Role());

        superAdmin.setNome("SUPER_ADMIN");
        superAdmin.setDescricao("Administrador total");
        superAdmin.setPermissions(permissionRepository.findAll());
        roleRepository.save(superAdmin);


        // GERENTE - faz quase tudo, menos criar/editar/deletar loja
        Role gerente = roleRepository.findByNome("GERENTE").orElse(new Role());

        gerente.setNome("GERENTE");
        gerente.setDescricao("Gerente da farmácia");

        gerente.setPermissions(permissionRepository.findByNomeIn(List.of("PRODUCT_READ", "PRODUCT_CREATE", "PRODUCT_UPDATE", "PRODUCT_DELETE",

                "ESTOQUE_READ", "ESTOQUE_CREATE", "ESTOQUE_UPDATE", "ESTOQUE_DELETE",

                "PEDIDO_READ", "PEDIDO_CREATE", "PEDIDO_UPDATE", "PEDIDO_DELETE",

                "PRODUTO_READ", "PRODUTO_CREATE", "PRODUTO_UPDATE", "PRODUTO_DELETE",

                "USUARIO_READ", "USUARIO_CREATE", "USUARIO_UPDATE", "USUARIO_DELETE",

                "CATEGORIA_READ", "CATEGORIA_CREATE", "CATEGORIA_UPDATE", "CATEGORIA_DELETE",

                "BAIRRO_READ", "BAIRRO_CREATE", "BAIRRO_UPDATE", "BAIRRO_DELETE",

                "LOJA_BAIRRO_READ", "LOJA_BAIRRO_CREATE", "LOJA_BAIRRO_UPDATE", "LOJA_BAIRRO_DELETE",

                "PERMISSIONS_READ",

                // pode ver loja, mas não criar/editar/deletar
                "LOJA_READ")));

        roleRepository.save(gerente);


        // ATENDENTE - balcão / pedidos
        Role atendente = roleRepository.findByNome("ATENDENTE").orElse(new Role());

        atendente.setNome("ATENDENTE");
        atendente.setDescricao("Atendente da farmácia");

        atendente.setPermissions(permissionRepository.findByNomeIn(List.of("PRODUCT_READ", "PRODUTO_READ", "ESTOQUE_READ", "PEDIDO_READ", "PEDIDO_CREATE", "PEDIDO_UPDATE", "CATEGORIA_READ")));

        roleRepository.save(atendente);


        // REPOSITOR - mexe mais com estoque
        Role repositor = roleRepository.findByNome("REPOSITOR").orElse(new Role());

        repositor.setNome("REPOSITOR");
        repositor.setDescricao("Repositor / controle de estoque");

        repositor.setPermissions(permissionRepository.findByNomeIn(List.of("PRODUCT_READ", "PRODUTO_READ", "ESTOQUE_READ", "ESTOQUE_CREATE", "ESTOQUE_UPDATE", "CATEGORIA_READ")));

        roleRepository.save(repositor);


        // CAIXA - finalização de pedidos
        Role caixa = roleRepository.findByNome("CAIXA").orElse(new Role());

        caixa.setNome("CAIXA");
        caixa.setDescricao("Operador de caixa");

        caixa.setPermissions(permissionRepository.findByNomeIn(List.of("PRODUCT_READ", "PRODUTO_READ", "ESTOQUE_READ", "PEDIDO_READ", "PEDIDO_CREATE", "PEDIDO_UPDATE")));

        roleRepository.save(caixa);


        // FARMACEUTICO - valida e acompanha pedidos/estoque
        Role farmaceutico = roleRepository.findByNome("FARMACEUTICO").orElse(new Role());

        farmaceutico.setNome("FARMACEUTICO");
        farmaceutico.setDescricao("Farmacêutico responsável");

        farmaceutico.setPermissions(permissionRepository.findByNomeIn(List.of("PRODUCT_READ", "PRODUTO_READ", "ESTOQUE_READ", "ESTOQUE_UPDATE", "PEDIDO_READ", "PEDIDO_UPDATE", "CATEGORIA_READ")));

        roleRepository.save(farmaceutico);
    }


    private void criarAdmin() {

        if (userRepository.findByUsername("suporte1").isEmpty()) {

            Role role = roleRepository.findByNome("SUPER_ADMIN").orElseThrow();

            User user = new User();

            user.setUsername("suporte1");

            user.setPassword(passwordEncoder.encode("123"));

            user.setEmail("admin@email.com");

            user.setAtivo(true);

            user.setRole(role);

            userRepository.save(user);

            System.out.println("Admin criado!");
        }
    }
}