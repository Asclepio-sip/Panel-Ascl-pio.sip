package Asclepio.Usuario.User.Controller;

import Asclepio.Usuario.User.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import Asclepio.Usuario.User.Controller.API.UserAPI;
import Asclepio.Usuario.User.User;
import Asclepio.Usuario.User.UserService;
import projeto_base_de_telas_e_login.Usuario.User.dto.*;
import Asclepio.config.security.TokenService;

import java.util.UUID;

@RestController
public class ControllerUsuario implements UserAPI {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public ControllerUsuario(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {

        var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        var auth = authenticationManager.authenticate(authToken);

        var user = (User) auth.getPrincipal();

        var token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO dto) {
        var user = userService.createUser(dto);
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    public ResponseEntity<Page<ResponseListaDeUserDTO>> listarUsuarios(UserFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(userService.lista(filtro, pageable));
    }
    public ResponseEntity<Void> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok().build();
    }
}