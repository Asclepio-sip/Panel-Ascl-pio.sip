package Asclepio.ProdutoVariacao.controller.api;

import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoResponse;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoFiltro;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/variacoes")
@Tag(name = "Produto Variações")
public interface ProdutoVariacaoApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerProdutoVariacao') or hasAuthority('CriarProdutoVariacao')")
    ResponseEntity<Page<ProdutoVariacaoResponse>> listar(
            @ParameterObject ProdutoVariacaoFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @PostMapping("/produtos/{produtoId}")
    @PreAuthorize("hasAuthority('CriarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponse> criar(
            @PathVariable Long produtoId,
            @RequestBody ProdutoVariacaoRequest dto
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarProdutoVariacao') or hasAuthority('CriarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoVariacaoUpdateRequest dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirProdutoVariacao')")
    ResponseEntity<Void> deletar(@PathVariable Long id);

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorias com filtros")
    @PreAuthorize("hasAuthority('VerProdutoVariacao')")
    ResponseEntity<CategoriaPageResponse> listar(
            @ParameterObject CategoriaFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "Listar produtos", description = """
            Lista os produtos com paginação e filtros opcionais.
            
            Filtros disponíveis:
            - nome: filtra pelo nome do produto
            - categoriaId: filtra pelo ID da categoria
            - nomeCategoria: filtra pelo nome da categoria
            
            Exemplos:
            
            Listar todos:
            /variacoes/produtos?page=0&size=10
            
            Buscar por nome:
            /variacoes/produtos?nome=dipirona&page=0&size=10
            
            Buscar por categoria:
            /variacoes/produtos?categoriaId=1&page=0&size=10
            
            Buscar por nome da categoria:
            /variacoes/produtos?nomeCategoria=medicamento&page=0&size=10
            
            Buscar por nome e categoria:
            /variacoes/produtos?nome=dipirona&categoriaId=1&page=0&size=10
            
            Ordenar por nome:
            /variacoes/produtos?page=0&size=10&sort=nome,asc
            """)
    @GetMapping("/produtos")
    @PreAuthorize("hasAuthority('VerProduto')")
    ResponseEntity<Page<ProdutoResponse>> listar(
            @ParameterObject ProdutoFiltro filtro,
            @ParameterObject Pageable pageable
    );
}