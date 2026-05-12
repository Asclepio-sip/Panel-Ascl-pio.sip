package projeto_base_de_telas_e_login.Produto.TelaProdutos.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Produto.Product;

import java.util.List;

@RequestMapping("/products")
@Tag(name = "Produtos")
public interface ProdutoApi {

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    ResponseEntity<List<Product>> listar();

    @PostMapping
    @Operation(summary = "Criar produto")
    ResponseEntity<Product> criar(
            @RequestBody Product product
    );

    @PutMapping("/{id}")
    @Operation(summary = "Editar produto")
    ResponseEntity<Product> editar(
            @PathVariable Long id,
            @RequestBody Product product
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}