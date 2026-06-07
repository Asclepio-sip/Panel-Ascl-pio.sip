package projeto_base_de_telas_e_login.Usuario.Role.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.Usuario.Role.Controller.api.RoleApi;
import projeto_base_de_telas_e_login.Usuario.Role.Role;
import projeto_base_de_telas_e_login.Usuario.Role.ServiceRole;

import java.util.List;

@RestController
public class ControllerRota implements RoleApi {

    private ServiceRole  serviceRole;

    public ControllerRota(ServiceRole serviceRole) {
        this.serviceRole = serviceRole;
    }

    public ResponseEntity<List<Role>> listaDeRole(){
        return ResponseEntity.ok( serviceRole.listaDeRole());
    }
}
