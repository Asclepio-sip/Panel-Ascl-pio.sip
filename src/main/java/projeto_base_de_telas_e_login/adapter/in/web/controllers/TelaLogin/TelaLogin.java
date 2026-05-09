package projeto_base_de_telas_e_login.adapter.in.web.controllers.TelaLogin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.adapter.in.web.dto.User.AuthenticationDTO;
import projeto_base_de_telas_e_login.adapter.in.web.dto.User.LoginResponseDTO;
import projeto_base_de_telas_e_login.adapter.out.persistence.User.UserEntity;
import projeto_base_de_telas_e_login.config.security.TokenService;


@RestController
@RequestMapping("/auth")
public class TelaLogin {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public TelaLogin(
            AuthenticationManager authenticationManager,
            TokenService tokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid AuthenticationDTO data
    ) {
        var authToken = new UsernamePasswordAuthenticationToken(
                data.login(),
                data.password()
        );

        var auth = authenticationManager.authenticate(authToken);
        var user = (UserEntity) auth.getPrincipal();

        var token = tokenService.generateToken(user.toDomain());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
