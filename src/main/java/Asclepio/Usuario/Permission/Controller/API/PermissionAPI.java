package Asclepio.Usuario.Permission.Controller.API;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import Asclepio.Usuario.Permission.Permission;

import java.util.List;
@RequestMapping("/permission")
@Tag(name = "Permission", description = "Gerenciamento de Permission")
public interface PermissionAPI {



    @GetMapping
    @PreAuthorize("hasAuthority('VerPermissoes')")
    ResponseEntity<List<Permission>> Lista();
}
