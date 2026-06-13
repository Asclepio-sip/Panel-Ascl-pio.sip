package projeto_base_de_telas_e_login.Categoria.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Categoria.dto.CategoriaResponse;
import projeto_base_de_telas_e_login.Categoria.dto.CriarCategoria;

import java.util.List;

@RequestMapping("/categorias")
@Tag(name = "Categorias")
public interface CategoriaApi {

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    ResponseEntity<List<CategoriaResponse>> listar();

    @GetMapping("/principais")
    @Operation(summary = "Listar categorias principais")
    ResponseEntity<List<CategoriaResponse>> listarPrincipais();

    @GetMapping("/{categoriaPaiId}/subcategorias")
    @Operation(summary = "Listar subcategorias de uma categoria")
    ResponseEntity<List<CategoriaResponse>> listarSubcategorias(
            @PathVariable Long categoriaPaiId
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
    @PreAuthorize("hasAuthority('ExcluirUsuario')")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}