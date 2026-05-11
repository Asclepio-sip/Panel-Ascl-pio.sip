package projeto_base_de_telas_e_login.controllers.TelaProdutos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.controllers.TelaProdutos.api.ProdutoApi;
import projeto_base_de_telas_e_login.entidade.Product;
import projeto_base_de_telas_e_login.service.Produto.ProdutoService;

import java.util.List;

@RestController
public class TelaProdutos implements ProdutoApi {

    private final ProdutoService produtoService;

    public TelaProdutos(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public ResponseEntity<List<Product>> listar() {

        return ResponseEntity.ok(
                produtoService.listarTodos()
        );
    }

    @Override
    public ResponseEntity<Product> criar(Product product) {

        Product novo = produtoService.criar(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novo);
    }

    @Override
    public ResponseEntity<Product> editar(Long id, Product product) {

        return ResponseEntity.ok(
                produtoService.editar(id, product)
        );
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {

        produtoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}