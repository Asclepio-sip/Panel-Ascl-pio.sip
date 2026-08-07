package Asclepio.Produto.Controller.api;

import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/products")
@Tag(name = "Produtos")
public interface ProdutoApi {

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
    @GetMapping
    @PreAuthorize("hasAuthority('VerProduto')")
    ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(@ParameterObject ProdutoFiltro filtro, @ParameterObject Pageable pageable);

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar produto com imagem")
    @PreAuthorize("hasAuthority('CriarProduto')")
    ResponseEntity<ProdutoStorageResponse> criar(@RequestParam String nome, @RequestParam(required = false) String descricao, @RequestParam(required = false) String marca, @RequestParam Long categoriaId, @RequestParam(required = false) MultipartFile imagem);

    @PatchMapping("/{id}")
    @Operation(summary = "Editar produto")
    @PreAuthorize("hasAuthority('EditarProduto')")
    ResponseEntity<ProdutoStorageResponse> editar(@PathVariable Long id, @RequestBody ProdutoUpdateDto dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    @PreAuthorize("hasAuthority('ExcluirProduto')")
    ResponseEntity<Void> deletar(@PathVariable Long id);

    @PostMapping("/categorias")
    @Operation(summary = "Criar categoria ou subcategoria")
    @PreAuthorize("hasAuthority('CriarProduto')")
    ResponseEntity<CategoriaResponse> criar(@RequestBody CriarCategoria dto);


    @GetMapping("/categorias")
    @Operation(summary = "Listar categorias com filtros")
    @PreAuthorize("hasAuthority('VerCategoria')")
    ResponseEntity<CategoriaPageResponse> listar(@ParameterObject CategoriaFiltro filtro, @ParameterObject Pageable pageable);


    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CriarProduto')")
    @Operation(summary = "Cadastrar produto completo")
    ResponseEntity<CadastroProdutoCompletoResponse> cadastrar(@ModelAttribute @Valid CadastroProdutoCompletoDTO dto);
}