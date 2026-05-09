package projeto_base_de_telas_e_login.adapter.in.web.controllers.TelaProdutos;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Categoria.CriarCategoriaDTO;
import projeto_base_de_telas_e_login.domain.UseCase.Categoria.CategoriaUseCase;
import projeto_base_de_telas_e_login.domain.model.categoria.Categoria;
import projeto_base_de_telas_e_login.domain.repository.CategoriaPorta;

import java.util.List;


@RestController
@RequestMapping("categorias")
public class TelaCategoria {

    private final CategoriaUseCase categoriaUseCase;

    public TelaCategoria(CategoriaUseCase categoriaUseCase) {
        this.categoriaUseCase = categoriaUseCase;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaUseCase.listarTodas();
    }


    @PostMapping
    public Categoria criar(@RequestBody @Valid CriarCategoriaDTO dto) {
        return categoriaUseCase.criar(dto.nomeCategoria());
    }

    @PutMapping("/{id}")
    public Categoria editar(
            @PathVariable Long id,
            @RequestBody @Valid CriarCategoriaDTO dto
    ) {
        return categoriaUseCase.editar(id, dto.nomeCategoria());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            categoriaUseCase.deletar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}