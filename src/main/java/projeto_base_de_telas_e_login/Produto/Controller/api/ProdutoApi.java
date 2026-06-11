package projeto_base_de_telas_e_login.Produto.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
   // @PreAuthorize("hasAuthority('PRODUTO_READ')")
    ResponseEntity<Page<ProductoResponseDto>> listar(Pageable pageable);

    @PostMapping
    @Operation(summary = "Criar produto")
    @PreAuthorize("hasAuthority('PRODUTO_CREATE')")
    ResponseEntity<Product> criar(@RequestBody ProductoAddDto dto);

    @PatchMapping("/{id}")
    @Operation(summary = "Editar produto")
    @PreAuthorize("hasAuthority('PRODUTO_UPDATE')")
    ResponseEntity<ProductoResponseDto> editar(@PathVariable  Long id,@RequestBody ProdutoUpdateDto product);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    @PreAuthorize("hasAuthority('PRODUTO_DELETE')")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}