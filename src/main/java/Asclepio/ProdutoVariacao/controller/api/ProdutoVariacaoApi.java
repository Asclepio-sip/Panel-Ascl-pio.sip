package Asclepio.ProdutoVariacao.controller.api;

import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoPageResponse;
import Asclepio.ProdutoVariacao.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/variacoes")
public interface ProdutoVariacaoApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoPageResponse> listar(
            @ParameterObject ProdutoVariacaoFiltro filtro,
            @ParameterObject Pageable pageable
    );


    @PostMapping("/produtos/{produtoId}")
    @PreAuthorize("hasAuthority('CriarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponseDTO> criar(
            @PathVariable Long produtoId,
            @RequestBody ProdutoVariacaoAddDTO dto
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoVariacaoUpdateDTO dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirProdutoVariacao')")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );


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
    @GetMapping("/produtos")
    @PreAuthorize("hasAuthority('VerProduto')")
    ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(@ParameterObject ProdutoFiltro filtro, @ParameterObject Pageable pageable);

}