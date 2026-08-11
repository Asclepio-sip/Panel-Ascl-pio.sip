package Asclepio.Usuario.Role.Controller;

import Asclepio.Usuario.Role.dto.RoleResponseDTO;
import Asclepio.config.security.UsuarioAutenticado;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.Usuario.Role.Controller.api.RoleApi;
import Asclepio.Usuario.Role.Role;
import Asclepio.Usuario.Role.ServiceRole;

import java.util.List;


@RestController
public class ControllerRota implements RoleApi {

    private final ServiceRole serviceRole;

    public ControllerRota(ServiceRole serviceRole) {
        this.serviceRole = serviceRole;
    }

    @Override
    public ResponseEntity<List<RoleResponseDTO>> listaDeRole() {

        UsuarioAutenticado usuario = (UsuarioAutenticado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long empresaId = usuario.getEmpresaId();

        return ResponseEntity.ok(serviceRole.listaDeRole(empresaId));
    }
}