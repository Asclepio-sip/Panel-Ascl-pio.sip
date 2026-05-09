package projeto_base_de_telas_e_login.adapter.in.web.controllers.CriarUsuario;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.adapter.in.web.dto.User.LoginResponseDTO;
import projeto_base_de_telas_e_login.adapter.in.web.dto.User.RegisterDTO;
import projeto_base_de_telas_e_login.domain.UseCase.User.UserUseCase;
import projeto_base_de_telas_e_login.config.security.TokenService;

import projeto_base_de_telas_e_login.adapter.in.web.dto.User.UpdateUserDTO;
import projeto_base_de_telas_e_login.adapter.in.web.dto.User.UserResponseDTO;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/register")
public class CriarUsuario {

    private final UserUseCase userUseCase;
    private final TokenService tokenService;

    public CriarUsuario(UserUseCase userUseCase, TokenService tokenService) {
        this.userUseCase = userUseCase;
        this.tokenService = tokenService;
    }
    @PostMapping
    public ResponseEntity<LoginResponseDTO> register(
            @RequestBody @Valid RegisterDTO dto
    ) {
        var user = userUseCase.createUser(
                dto.login(),
                dto.password(),
                dto.role()
        );

        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        var users = userUseCase.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name(),
                        user.getPassword()
                ))
                .toList();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> atualizarUsuario(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDTO dto
    ) {
        userUseCase.updateUser(
                id,
                dto.login(),
                dto.password(),
                dto.role()
        );
        return ResponseEntity.ok().build();
    }
}
