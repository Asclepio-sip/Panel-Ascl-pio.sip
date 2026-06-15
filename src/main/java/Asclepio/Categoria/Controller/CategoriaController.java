package Asclepio.Categoria.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.Categoria.Controller.api.CategoriaApi;
import Asclepio.Categoria.CategoriaService;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;

import java.util.List;

@RestController
public class TelaCategoria implements CategoriaApi {

    private final CategoriaService categoriaService;

    public TelaCategoria(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(
                categoriaService.listarTodas()
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> listarPrincipais() {
        return ResponseEntity.ok(
                categoriaService.listarCategoriasPrincipais()
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> listarSubcategorias(Long categoriaPaiId) {
        return ResponseEntity.ok(
                categoriaService.listarSubcategorias(categoriaPaiId)
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<CategoriaResponse> criar(CriarCategoria dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CategoriaResponse.fromEntity(
                        categoriaService.criar(dto)
                )
        );
    }

    @Override
    public ResponseEntity<CategoriaResponse> editar(Long id, CriarCategoria dto) {
        return ResponseEntity.ok(
                CategoriaResponse.fromEntity(
                        categoriaService.editar(id, dto)
                )
        );
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}