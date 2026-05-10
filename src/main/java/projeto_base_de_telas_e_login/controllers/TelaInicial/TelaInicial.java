package projeto_base_de_telas_e_login.controllers.TelaInicial;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.dto.Pedido.PedidoAddDTO;
import projeto_base_de_telas_e_login.dto.Pedido.ProductListaDto;

import projeto_base_de_telas_e_login.persistence.categoria.Categoria;

import projeto_base_de_telas_e_login.service.Estoque.EstoqueService;
import projeto_base_de_telas_e_login.service.Pedido.PedidoService;
import projeto_base_de_telas_e_login.service.Produto.ProdutoService;

import projeto_base_de_telas_e_login.persistence.categoria.CategoriaRepository;

import java.util.List;

@RestController
@RequestMapping("/productsPublico")
public class TelaInicial {

    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final CategoriaRepository categoriaRepository;
    private final EstoqueService estoqueService;

    public TelaInicial(
            ProdutoService produtoService,
            PedidoService pedidoService,
            CategoriaRepository categoriaRepository,
            EstoqueService estoqueService
    ) {
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
        this.categoriaRepository = categoriaRepository;
        this.estoqueService = estoqueService;
    }

    @Operation(summary = "Lista todos os produtos")
    @GetMapping("/list")
    public ResponseEntity<List<ProductListaDto>> listarProdutos() {

        return ResponseEntity.ok(
                produtoService.listarTodos()
                        .stream()
                        .map(ProductListaDto::fromDomain)
                        .toList()
        );
    }

    @Operation(summary = "Lista produtos por nome da categoria")
    @GetMapping("/produtos/categoria/{nome}")
    public ResponseEntity<List<ProductListaDto>> listarPorCategoria(
            @PathVariable String nome
    ) {

        return ResponseEntity.ok(
                produtoService.listarPorCategoria(nome)
                        .stream()
                        .map(ProductListaDto::fromDomain)
                        .toList()
        );
    }

    @Operation(summary = "Lista todas as categorias")
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {

        return ResponseEntity.ok(
                categoriaRepository.findAll()
                        .stream()
                        .map(Categoria::getNomeCategoria)
                        .toList()
        );
    }

    @Operation(summary = "Criar pedido")
    @PostMapping("/pedidos")
    public ResponseEntity<Void> criarPedido(
            @RequestBody PedidoAddDTO dto
    ) {

        pedidoService.criarPedido(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Lista estoque")
    @GetMapping("/lista")
    public ResponseEntity<?> listar() {

        return ResponseEntity.ok(
                estoqueService.listarTodos()
        );
    }
}