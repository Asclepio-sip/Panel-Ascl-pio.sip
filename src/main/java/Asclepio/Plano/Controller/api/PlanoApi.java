package Asclepio.Plano.Controller.api;

import Asclepio.Plano.dto.PlanoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/planos")
@Tag(name = "Planos", description = "Planos de assinatura do Asclépio (cadastrados pelo painel SaaS)")
public interface PlanoApi {

    @GetMapping
    @SecurityRequirements
    @Operation(
            summary = "Listar planos",
            description = "Público (sem token): usado na landing page. Do mais barato para o mais caro. "
                    + "Os planos são criados/editados só pelo painel SaaS, que espelha aqui via /interno/planos."
    )
    ResponseEntity<List<PlanoResponseDTO>> listar();
}
