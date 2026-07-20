package Asclepio.Usuario.Permission;

import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.Role.RoleRepository;
import Asclepio.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService {

    private final PermissionRepository repository;
    private final RoleRepository roleRepository;


    public PermissionService(PermissionRepository repository,RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;

    }

    public List<Permission> listarTodos() {
        return repository.findAll();
    }

    public Permission buscarPorId(UUID id) {

        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permissão não encontrada"));
    }


    public void adicionarPermissoesSuperAdministrador(Role role) {


        List<Permission> permissoes = repository.findAll();


        role.setPermissions(permissoes);


        roleRepository.save(role);
    }

}