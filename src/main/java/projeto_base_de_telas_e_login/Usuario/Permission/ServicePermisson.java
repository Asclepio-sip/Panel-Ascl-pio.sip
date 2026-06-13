package projeto_base_de_telas_e_login.Usuario.Permission;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServicePermisson {

    private PermissionRepository permissionRepository;

    public ServicePermisson(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> ListaDePermission(){
        return permissionRepository.findAll();
    }
}
