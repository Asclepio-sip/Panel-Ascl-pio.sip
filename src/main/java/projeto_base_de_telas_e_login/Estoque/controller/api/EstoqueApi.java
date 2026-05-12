package projeto_base_de_telas_e_login.controllers.TelaEstoque.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.Estoque.Estoque.AplicarPromocaoDto;
import projeto_base_de_telas_e_login.Estoque.Estoque.EstoqueAddDto;
import projeto_base_de_telas_e_login.Estoque.Estoque.EstoqueListaDto;

import java.util.List;

@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Gerenciamento de estoque")
public interface EstoqueApi {

    @Operation(summary = "Criar item estoque")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    ResponseEntity<Void> criar(
            @RequestBody @Valid EstoqueAddDto dto
    );

    @Operation(summary = "Atualizar estoque")
    @ApiResponse(responseCode = "200", description = "Atualizado")
    @PutMapping
    ResponseEntity<Void> atualizar(
            @RequestBody @Valid EstoqueAddDto dto
    );

    @Operation(summary = "Aplicar promoção")
    @ApiResponse(responseCode = "200", description = "Promoção aplicada")
    @PatchMapping("/promocao")
    ResponseEntity<Void> aplicarPromocao(
            @RequestBody @Valid AplicarPromocaoDto dto
    );

    @Operation(summary = "Listar estoque")
    @GetMapping
    ResponseEntity<List<EstoqueListaDto>> listar();

    @Operation(summary = "Buscar por loja")
    @GetMapping("/loja")
    ResponseEntity<List<EstoqueListaDto>> buscarPorNomeLoja(
            @RequestParam String nome
    );

    @Operation(summary = "Buscar por produto")
    @GetMapping("/produto")
    ResponseEntity<List<EstoqueListaDto>> buscarPorNomeProduto(
            @RequestParam String nome
    );

    @Operation(summary = "Filtrar estoque")
    @GetMapping("/filtro")
    ResponseEntity<List<EstoqueListaDto>> filtrar(

            @RequestParam(required = false) Long lojaId,

            @RequestParam(required = false) String nomeLoja,

            @RequestParam(required = false) Boolean semEstoque
    );

    @Operation(summary = "Deletar item estoque")
    @ApiResponse(responseCode = "204", description = "Deletado")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}