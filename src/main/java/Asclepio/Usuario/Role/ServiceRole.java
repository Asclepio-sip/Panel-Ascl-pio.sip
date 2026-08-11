package Asclepio.Usuario.Role;

import Asclepio.Empresa.Empresa;
import Asclepio.Usuario.Permission.PermissionRepository;
import Asclepio.Usuario.Role.dto.RoleResponseDTO;
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
                "Gerente",
                "Administrador da empresa",
                permissionRepository.findAll()
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
                        "CriarProduto",
                        "VerEstoque",
                        "CriarEstoque",
                        "EditarEstoque",
                        "VerCategoria",
                        "CriarCategoria",
                        "VerProdutoVariacao",
                        "VerMovimentacaoEstoque",
                        "CriarProdutoVariacao",
                        "EditarCategoria",
                        "EditarProdutoVariacao"
                ))
        );

        criarRole(
                empresa,
                "Caixa",
                "Operador de caixa",
                permissionRepository.findByNomeIn(List.of(
                        "CriarPedido",
                        "VerPedido"
                ))
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


    public List<RoleResponseDTO> listaDeRole(Long empresaId) {

        return roleRepository
                .findAllByEmpresa_Id(empresaId)
                .stream()
                .map(RoleResponseDTO::from)
                .toList();
    }

    public Role buscarSuperAdministrador(Empresa empresa) {

        return roleRepository
                .findByNomeAndEmpresa("Gerente", empresa)
                .orElseThrow(() ->
                        new RuntimeException("Administrador não encontrado"));
    }

}