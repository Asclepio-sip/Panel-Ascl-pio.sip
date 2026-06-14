package projeto_base_de_telas_e_login.Produto.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.Produto.dto.ProductoAddDto;
import projeto_base_de_telas_e_login.Produto.dto.ProductoResponseDto;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoFiltro;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoUpdateDto;

import java.util.List;

@RequestMapping("/products")
@Tag(name = "Produtos")
public interface ProdutoApi {

    @Operation(summary = "Listar produtos", description = """
            Lista os produtos com paginação e filtros opcionais.
            
            Filtros disponíveis:
            - nome: filtra pelo nome do produto
            - variacao: filtra pela variação do produto
            - categoriaId: filtra pelo ID da categoria
            - nomeCategoria: filtra pelo nome da categoria
            
            Exemplos:
            
            Listar todos:
            /produtos?page=0&size=10
            
            Buscar por nome:
            /produtos?nome=dipirona&page=0&size=10
            
            Buscar por categoria:
            /produtos?categoriaId=1&page=0&size=10
            
            Buscar por nome da categoria:
            /produtos?nomeCategoria=medicamento&page=0&size=10
            
            Buscar por nome e categoria:
            /produtos?nome=dipirona&categoriaId=1&page=0&size=10
            
            Ordenar por nome:
            /produtos?page=0&size=10&sort=name,asc
            """)
    @GetMapping
    @PreAuthorize("hasAuthority('VerProduto')")
    ResponseEntity<Page<ProductoResponseDto>> listar(@ParameterObject ProdutoFiltro filtro, @ParameterObject Pageable pageable);

    @PostMapping
    @Operation(summary = "Criar produto")
    @PreAuthorize("hasAuthority('CriarEstoque')")
    ResponseEntity<Product> criar(@RequestBody ProductoAddDto dto);

    @PatchMapping("/{id}")
    @Operation(summary = "Editar produto")
    @PreAuthorize("hasAuthority('EditarEstoque')")
    ResponseEntity<ProductoResponseDto> editar(@PathVariable Long id, @RequestBody ProdutoUpdateDto product);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    @PreAuthorize("hasAuthority('ExcluirEstoque')")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}