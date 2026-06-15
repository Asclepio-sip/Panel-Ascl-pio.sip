package Asclepio.Usuario.Permission.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.Usuario.Permission.Controller.API.PermissionAPI;
import Asclepio.Usuario.Permission.Permission;
import Asclepio.Usuario.Permission.ServicePermisson;

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
