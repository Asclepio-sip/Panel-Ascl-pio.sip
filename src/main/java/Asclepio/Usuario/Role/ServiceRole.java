package Asclepio.Usuario.Role;

import Asclepio.Empresa.Empresa;
import Asclepio.Usuario.Permission.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRole {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public ServiceRole(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public void criarRolesPadrao(Empresa empresa) {

        criarRole(
                empresa,
                "SuperAdministrador",
                "Administrador principal da empresa",
                permissionRepository.findAll()
        );

        criarRole(
                empresa,
                "Gerente",
                "Gerente da farmácia",
                permissionRepository.findByNomeIn(List.of(
                        "VerProduto", "CriarProduto", "EditarProduto", "ExcluirProduto",

                        "VerEstoque", "CriarEstoque", "EditarEstoque", "ExcluirEstoque", "PromocaoEstoque",

                        "VerPedido", "CriarPedido", "EditarPedido", "ExcluirPedido",

                        "VerUsuario", "CriarUsuario", "EditarUsuario", "ExcluirUsuario",

                        "VerCategoria", "CriarCategoria", "EditarCategoria", "ExcluirCategoria",

                        "VerBairro", "CriarBairro", "EditarBairro", "ExcluirBairro",

                        "VerLojaBairro", "CriarLojaBairro", "EditarLojaBairro", "ExcluirLojaBairro",

                        "VerEmpresa", "CriarEmpresa", "EditarEmpresa", "ExcluirEmpresa",

                        "VerPermissoes",

                        "VerProdutoVariacao",
                        "CriarProdutoVariacao",
                        "EditarProdutoVariacao",
                        "ExcluirProdutoVariacao",

                        "VerLoja",    "VerMovimentacaoEstoque"
                ))
        );

        criarRole(
                empresa,
                "Atendente",
                "Atendente da farmácia",
                permissionRepository.findByNomeIn(List.of(
                        "VerProduto",
                        "VerEstoque",
                        "VerPedido",
                        "CriarPedido",
                        "EditarPedido",
                        "VerProdutoVariacao",
                        "VerCategoria"
                ))
        );

        criarRole(
                empresa,
                "Repositor",
                "Repositor / controle de estoque",
                permissionRepository.findByNomeIn(List.of(
                        "VerProduto",
                        "VerEstoque",
                        "CriarEstoque",
                        "EditarEstoque",
                        "VerCategoria",
                        "VerProdutoVariacao"
                ))
        );

        criarRole(
                empresa,
                "Caixa",
                "Operador de caixa",
                permissionRepository.findByNomeIn(List.of(
                        "VerProduto",
                        "VerEstoque",
                        "VerPedido",
                        "CriarPedido",
                        "VerProdutoVariacao",
                        "EditarPedido"
                ))
        );

        criarRole(
                empresa,
                "Farmaceutico",
                "Farmacêutico responsável",
                permissionRepository.findByNomeIn(List.of(
                        "VerProduto",
                        "VerEstoque",
                        "EditarEstoque",
                        "VerPedido",
                        "EditarPedido",
                        "VerProdutoVariacao",
                        "VerCategoria"
                ))
        );

        criarRole(
                empresa,
                "Generico",
                "Usuário sem permissões",
                List.of()
        );
    }

    @Transactional
    public Role criarRole(
            Empresa empresa,
            String nome,
            String descricao,
            List permissions
    ) {

        return roleRepository
                .findByNomeAndEmpresa(nome, empresa)
                .orElseGet(() -> {

                    Role role = new Role();

                    role.setNome(nome);
                    role.setDescricao(descricao);
                    role.setEmpresa(empresa);
                    role.setPermissions(permissions);

                    return roleRepository.save(role);
                });
    }

    public List<Role> listaDeRole() {
        return roleRepository.findAll();
    }

    public Role buscarSuperAdministrador(Empresa empresa) {

        return roleRepository
                .findByNomeAndEmpresa("SuperAdministrador", empresa)
                .orElseThrow(() ->
                        new RuntimeException("SuperAdministrador não encontrado"));
    }

}