package Asclepio.Usuario.Role.Controller.api;

import Asclepio.Usuario.Role.dto.RoleResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import Asclepio.Usuario.Role.Role;

import java.util.List;

@RequestMapping("/Role")
@Tag(name = "RoleApi", description = "Gerenciamento de Permission")
public interface RoleApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerRole')")
    ResponseEntity<List<RoleResponseDTO>> listaDeRole() ;
}
