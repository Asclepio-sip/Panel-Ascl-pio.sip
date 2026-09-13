package Asclepio.Produto.Controller.api;

import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoResponse;
import Asclepio.Produto.dto.ProdutoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
            /products?page=0&size=10
            
            Buscar por nome:
            /products?nome=dipirona&page=0&size=10
            
            Buscar por categoria:
            /products?categoriaId=1&page=0&size=10
            
            Buscar por nome da categoria:
            /products?nomeCategoria=medicamento&page=0&size=10
            
            Buscar por nome e categoria:
            /products?nome=dipirona&categoriaId=1&page=0&size=10
            
            Ordenar por nome:
            /products?page=0&size=10&sort=nome,asc
            """)
    @GetMapping
    @PreAuthorize("hasAuthority('VerProduto')")
    ResponseEntity<Page<ProdutoResponse>> listar(@ParameterObject ProdutoFiltro filtro, @ParameterObject Pageable pageable);

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar produto com imagem")
    @PreAuthorize("hasAuthority('CriarProduto')")
    ResponseEntity<ProdutoResponse> criar(
            @RequestParam String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) MultipartFile imagem
    );

    @PatchMapping("/{id}")
    @Operation(summary = "Editar produto")
    @PreAuthorize("hasAuthority('EditarProduto')")
    ResponseEntity<ProdutoResponse> editar(@PathVariable Long id, @RequestBody ProdutoUpdateRequest dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto")
    @PreAuthorize("hasAuthority('ExcluirProduto')")
    ResponseEntity<Void> deletar(@PathVariable Long id);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    @PreAuthorize("hasAuthority('VerProduto')")
    ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id);

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

    @PatchMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Atualizar imagem do produto")
    @PreAuthorize("hasAuthority('EditarProduto')")
    ResponseEntity<ProdutoResponse> atualizarImagem(@PathVariable Long id, @RequestParam MultipartFile imagem);
}