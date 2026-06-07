package projeto_base_de_telas_e_login.Usuario.Permission.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.Usuario.Permission.Controller.API.PermissionAPI;
import projeto_base_de_telas_e_login.Usuario.Permission.Permission;
import projeto_base_de_telas_e_login.Usuario.Permission.ServicePermisson;

import java.util.List;

@RestController
public class ControllerPermission implements PermissionAPI {

    private ServicePermisson servicePermisson;

    public ControllerPermission(ServicePermisson servicePermisson) {
        this.servicePermisson = servicePermisson;
    }

    @Override
    public ResponseEntity<List<Permission>> Lista(){
        return ResponseEntity.ok(servicePermisson.ListaDePermission());
    }
}
