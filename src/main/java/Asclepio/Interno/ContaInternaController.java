package Asclepio.Interno;

import Asclepio.Usuario.User.User;
import Asclepio.Usuario.User.UserService;
import Asclepio.Usuario.User.dto.RequestCriarContaDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Rotas chamadas só pelo back-end do painel SaaS (header X-Api-Key, ver InternoApiKeyFilter).
 * Criar conta saiu de /user/CriarConta (era público): agora só o admin do SaaS cria contas.
 */
@Hidden
@RestController
@RequestMapping("/interno")
public class ContaInternaController {

    private final UserService userService;
    private final AcessoContaService acessoContaService;
    private final PlanoContaService planoContaService;

    public ContaInternaController(UserService userService, AcessoContaService acessoContaService,
                                  PlanoContaService planoContaService) {
        this.userService = userService;
        this.acessoContaService = acessoContaService;
        this.planoContaService = planoContaService;
    }

    /**
     * Cria empresa + loja + usuário Super Administrador (mesma regra do antigo /user/CriarConta)
     * já no plano escolhido (planoId opcional).
     */
    @PostMapping("/contas")
    public ResponseEntity<ContaCriadaDTO> criarConta(@RequestBody @Valid RequestCriarContaDTO dto) {
        // Confere o plano antes de criar, para não sobrar conta sem o plano certo.
        if (dto.planoId() != null) {
            planoContaService.buscarPlano(dto.planoId());
        }
        User user = userService.criarConta(dto);
        if (dto.planoId() != null) {
            planoContaService.definirPlano(user.getId(), dto.planoId());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ContaCriadaDTO(user.getId(), user.getNome(), user.getEmail()));
    }

    /**
     * Bloqueia (ativo = false) ou libera (ativo = true) a empresa do usuário dono da conta.
     * Bloqueada: ninguém da empresa faz login e os tokens já emitidos passam a dar 403.
     */
    @PutMapping("/contas/{userId}/acesso")
    public ResponseEntity<Void> alterarAcesso(@PathVariable UUID userId, @RequestBody @Valid AcessoContaDTO dto) {
        acessoContaService.alterarAcesso(userId, dto.ativo());
        return ResponseEntity.noContent().build();
    }

    /** Troca o plano da conta (empresa do usuário dono). Os limites valem na hora, sem novo login. */
    @PutMapping("/contas/{userId}/plano")
    public ResponseEntity<Void> alterarPlano(@PathVariable UUID userId, @RequestBody PlanoContaDTO dto) {
        planoContaService.definirPlano(userId, dto.planoId());
        return ResponseEntity.noContent().build();
    }
}
