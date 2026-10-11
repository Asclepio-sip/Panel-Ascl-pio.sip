package Asclepio.Interno;

import Asclepio.Plano.PlanoService;
import Asclepio.Plano.dto.PlanoResponseDTO;
import Asclepio.Plano.dto.PlanoSyncDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Planos: só o back-end do painel SaaS escreve (header X-Api-Key, ver InternoApiKeyFilter).
 * POST e PUT são "criar ou atualizar" pelo saasId, então reenviar não duplica.
 */
@Hidden
@RestController
@RequestMapping("/interno/planos")
public class PlanoInternoController {

    private final PlanoService planoService;

    public PlanoInternoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    /** Plano novo no SaaS. O saasId vem no corpo. */
    @PostMapping
    public ResponseEntity<PlanoResponseDTO> criar(@RequestBody @Valid PlanoSyncDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planoService.salvar(dto.saasId(), dto));
    }

    /** Plano editado no SaaS (se ainda não existir aqui, cria). */
    @PutMapping("/{saasId}")
    public ResponseEntity<PlanoResponseDTO> atualizar(@PathVariable Long saasId, @RequestBody @Valid PlanoSyncDTO dto) {
        return ResponseEntity.ok(planoService.salvar(saasId, dto));
    }

    /** Plano excluído no SaaS. */
    @DeleteMapping("/{saasId}")
    public ResponseEntity<Void> remover(@PathVariable Long saasId) {
        planoService.remover(saasId);
        return ResponseEntity.noContent().build();
    }
}
