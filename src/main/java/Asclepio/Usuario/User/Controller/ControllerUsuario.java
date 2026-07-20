package Asclepio.Usuario.User.Controller;

import Asclepio.Usuario.StorageWakeUpService;
import Asclepio.Usuario.User.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import Asclepio.Usuario.User.Controller.API.UserAPI;
import Asclepio.config.security.UsuarioAutenticado;
import Asclepio.Usuario.User.UserService;
import Asclepio.config.security.TokenService;

import java.util.UUID;

@RestController
public class ControllerUsuario implements UserAPI {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final StorageWakeUpService storageWakeUpService;

    public ControllerUsuario(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager, StorageWakeUpService storageWakeUpService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.storageWakeUpService = storageWakeUpService;
    }

    @Override
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {

        var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(authToken);
        var usuarioAutenticado = (UsuarioAutenticado) auth.getPrincipal();
        var token = tokenService.generateToken(usuarioAutenticado);
        storageWakeUpService.acordarStorage();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Override
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO dto) {

        userService.createUser(dto);

        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<LoginResponseDTO> CriarConta(@RequestBody @Valid RequestCriarContaDTO dto) {

        userService.criarConta(dto);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<ResponseListaDeUserDTO>> listarUsuarios(UserFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(userService.lista(filtro, pageable));
    }

    @Override
    public ResponseEntity<Void> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UpdateUserDTO dto) {
        userService.updateUser(id, dto);
        return ResponseEntity.ok().build();
    }
}