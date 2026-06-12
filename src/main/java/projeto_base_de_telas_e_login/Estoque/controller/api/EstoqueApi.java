package projeto_base_de_telas_e_login.Estoque.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Estoque.dto.*;

@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Gerenciamento de estoque")
public interface EstoqueApi {

    @Operation(summary = "Criar item estoque")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    @PreAuthorize("hasAuthority('CriarEstoque')")
    ResponseEntity<Void> criar(@RequestBody @Valid EstoqueAddDto dto);

    @Operation(summary = "Atualizar estoque")
    @ApiResponse(responseCode = "200", description = "Atualizado")
    @PatchMapping
    @PreAuthorize("hasAuthority('EditarEstoque')")
    ResponseEntity<Void> atualizar(@RequestBody @Valid AtulizarResquet dto);

    @Operation(summary = "Aplicar promoção")
    @ApiResponse(responseCode = "200", description = "Promoção aplicada")
    @PatchMapping("/promocao")
    @PreAuthorize("hasAuthority('PromocaoEstoque')")
    ResponseEntity<Void> aplicarPromocao(@RequestBody @Valid AplicarPromocaoDto dto);

    @GetMapping("/relatorio")
//    @PreAuthorize("hasAuthority('VerEstoque')")
    ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(EstoqueFiltro filtro, Pageable pageable);

    @Operation(summary = "Deletar item estoque")
    @ApiResponse(responseCode = "204", description = "Deletado")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirEstoque')")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}