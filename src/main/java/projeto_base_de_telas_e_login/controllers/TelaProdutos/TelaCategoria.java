package projeto_base_de_telas_e_login.controllers.TelaProdutos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.controllers.TelaProdutos.api.CategoriaApi;
import projeto_base_de_telas_e_login.dto.Categoria.CriarCategoriaDTO;
import projeto_base_de_telas_e_login.persistence.categoria.Categoria;
import projeto_base_de_telas_e_login.service.Categoria.CategoriaService;

import java.util.List;

@RestController
public class TelaCategoria implements CategoriaApi {

    private final CategoriaService categoriaService;

    public TelaCategoria(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public ResponseEntity<List<Categoria>> listar() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @Override
    public ResponseEntity<Categoria> criar(CriarCategoriaDTO dto) {
        Categoria categoria = categoriaService.criar(dto.nomeCategoria());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @Override
    public ResponseEntity<Categoria> editar(Long id, CriarCategoriaDTO dto) {
        Categoria categoria = categoriaService.editar(id, dto.nomeCategoria());
        return ResponseEntity.ok(categoria);
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}