package Asclepio.Categoria.Controller;

import Asclepio.Categoria.CategoriaService;
import Asclepio.Categoria.Controller.api.CategoriaApi;
import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoriaController implements CategoriaApi {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public ResponseEntity<CategoriaPageResponse> listar(
            CategoriaFiltro filtro,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                categoriaService.listar(filtro, pageable)
        );
    }

    @Override
    public ResponseEntity<CategoriaResponse> criar(CriarCategoria dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                categoriaService.criar(dto)
        );
    }

    @Override
    public ResponseEntity<CategoriaResponse> editar(Long id, CriarCategoria dto) {
        return ResponseEntity.ok(
                categoriaService.editar(id, dto)
        );
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}