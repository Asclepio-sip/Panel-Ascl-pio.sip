package projeto_base_de_telas_e_login.Usuario.User.Controller.API;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('CriarUser')")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO dto) ;

    @GetMapping
    @PreAuthorize("hasAuthority('VerUser')")
    public ResponseEntity<Page<ResponseListaDeUserDTO>>  listarUsuarios(Pageable pageable);

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('EditarUser')")
    public ResponseEntity<Void> atualizarUsuario( @PathVariable UUID id,@RequestBody @Valid UpdateUserDTO dto);
}
