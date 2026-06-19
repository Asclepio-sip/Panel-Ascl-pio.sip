package Asclepio.Categoria.Controller.api;

import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/categorias")
@Tag(name = "Categorias")
public interface CategoriaApi {

    @GetMapping
    @Operation(summary = "Listar categorias com filtros")
    @PreAuthorize("hasAuthority('VerCategoria')")
    ResponseEntity<CategoriaPageResponse> listar(
            @ParameterObject CategoriaFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @PostMapping
    @Operation(summary = "Criar categoria ou subcategoria")
    @PreAuthorize("hasAuthority('CriarCategoria')")
    ResponseEntity<CategoriaResponse> criar(
            @RequestBody CriarCategoria dto
    );

    @PutMapping("/{id}")
    @Operation(summary = "Editar categoria")
    @PreAuthorize("hasAuthority('EditarCategoria')")
    ResponseEntity<CategoriaResponse> editar(
            @PathVariable Long id,
            @RequestBody CriarCategoria dto
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria")
    @PreAuthorize("hasAuthority('ExcluirCategoria')")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}