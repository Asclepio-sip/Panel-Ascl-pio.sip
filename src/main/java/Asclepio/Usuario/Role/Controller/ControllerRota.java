package Asclepio.Usuario.Role.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.Usuario.Role.Controller.api.RoleApi;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.Role.ServiceRole;

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
