package Asclepio.Loja.LojaBairro.Controller.api;

import Asclepio.Loja.LojaBairro.dto.CreateLojaBairroRequest;
import Asclepio.Loja.LojaBairro.dto.LojaBairroFiltroDTO;
import Asclepio.Loja.LojaBairro.dto.LojaBairroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/loja-bairros")
@Tag(name = "Loja Bairro", description = "Gerenciamento dos bairros atendidos por cada loja")
public interface LojaBairroApi {

    @PostMapping
    @PreAuthorize("hasAuthority('CriarLojaBairro')")
    @Operation(
            summary = "Vincular bairro à loja",
            description = "Cria um vínculo entre uma loja e um bairro, definindo o valor do frete."
    )
    ResponseEntity<LojaBairroResponse> criar(@RequestBody CreateLojaBairroRequest request);

    @GetMapping
    @PreAuthorize("hasAuthority('VerLojaBairro')")
    @Operation(
            summary = "Listar vínculos com filtros",
            description = """
                    Lista os vínculos entre lojas e bairros de forma paginada.
                    
                    Filtros disponíveis:
                    - id: ID do vínculo.
                    - lojaId: ID da loja.
                    - nomeLoja: busca parcial pelo nome da loja.
                    - bairroId: ID do bairro.
                    - nomeBairro: busca parcial pelo nome do bairro.
                    - valorFreteMin: valor mínimo do frete.
                    - valorFreteMax: valor máximo do frete.
                    
                    Paginação:
                    - page: número da página.
                    - size: quantidade por página.
                    - sort: campo de ordenação. Exemplo: valorFrete,asc
                    """
    )
    ResponseEntity<Page<LojaBairroResponse>> listar(
            @ParameterObject LojaBairroFiltroDTO filtro,
            @ParameterObject Pageable pageable
    );

    @PutMapping("/{id}/frete")
    @PreAuthorize("hasAuthority('EditarLojaBairro')")
    @Operation(
            summary = "Atualizar valor do frete",
            description = "Atualiza apenas o valor do frete do vínculo entre loja e bairro."
    )
    ResponseEntity<LojaBairroResponse> atualizarFrete(
            @Parameter(description = "ID do vínculo", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Novo valor do frete", example = "7.50")
            @RequestParam BigDecimal valorFrete
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirLojaBairro')")
    @Operation(
            summary = "Excluir vínculo",
            description = "Remove o vínculo entre loja e bairro pelo ID."
    )
    ResponseEntity<Void> deletar(
            @Parameter(description = "ID do vínculo", example = "1")
            @PathVariable Long id
    );
}