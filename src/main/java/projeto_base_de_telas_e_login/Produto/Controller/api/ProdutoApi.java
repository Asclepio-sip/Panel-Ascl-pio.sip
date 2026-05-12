package projeto_base_de_telas_e_login.Produto.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.Produto.dto.ProductoAddDto;
import projeto_base_de_telas_e_login.Produto.dto.ProductoResponseDto;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoUpdateDto;

import java.util.List;

@RequestMapping("/products")
@Tag(name = "Produtos")
public interface ProdutoApi {

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    ResponseEntity<List<ProductoResponseDto>> listar();
    @PostMapping
    @Operation(summary = "Criar produto")
    ResponseEntity<Product> criar(
            @RequestBody ProductoAddDto dto
    );

    @PatchMapping("/{id}")
    @Operation(summary = "Editar produto")
    ResponseEntity<ProductoResponseDto> editar(Long id, ProdutoUpdateDto product);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}