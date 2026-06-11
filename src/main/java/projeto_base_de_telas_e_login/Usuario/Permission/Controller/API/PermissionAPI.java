package projeto_base_de_telas_e_login.Usuario.Permission.Controller.API;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projeto_base_de_telas_e_login.Usuario.Permission.Permission;

import java.util.List;
@RequestMapping("/permission")
@Tag(name = "Permission", description = "Gerenciamento de Permission")
public interface PermissionAPI {



    @GetMapping
    ResponseEntity<List<Permission>> Lista();
}
