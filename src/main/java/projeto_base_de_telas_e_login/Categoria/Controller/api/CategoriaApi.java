package projeto_base_de_telas_e_login.Categoria.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Categoria.dto.CriarCategoria;
import projeto_base_de_telas_e_login.Categoria.Categoria;

import java.util.List;

@RequestMapping("/categorias")
@Tag(name = "Categorias")
public interface CategoriaApi {

    @GetMapping
    @Operation(summary = "Listar categorias")
    ResponseEntity<List<Categoria>> listar();

    @PostMapping
    @Operation(summary = "Criar categoria")
    ResponseEntity<Categoria> criar( @RequestBody @Valid CriarCategoria dto);

    @PutMapping("/{id}")
    @Operation(summary = "Editar categoria")
    ResponseEntity<Categoria> editar( @PathVariable Long id, @RequestBody @Valid CriarCategoria dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}