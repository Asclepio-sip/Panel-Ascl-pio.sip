package projeto_base_de_telas_e_login.Usuario.User.Controller.API;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Usuario.User.User;
import projeto_base_de_telas_e_login.Usuario.User.dto.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/user")
@Tag(name = "user", description = "Gerenciamento de user")
public interface UserAPI {


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) ;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO dto) ;

    @GetMapping
    public ResponseEntity<List<ResponseListaDeUserDTO>>  listarUsuarios();

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> atualizarUsuario( @PathVariable UUID id,@RequestBody @Valid UpdateUserDTO dto);
}
