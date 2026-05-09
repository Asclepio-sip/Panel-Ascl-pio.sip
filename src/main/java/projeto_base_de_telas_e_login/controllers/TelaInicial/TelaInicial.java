package projeto_base_de_telas_e_login.controllers.TelaInicial;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.dto.Pedido.ProductListaDto;
import projeto_base_de_telas_e_login.service.Estoque.EstoqueUseCase;
import projeto_base_de_telas_e_login.service.Pedido.PedidoUserCase;
import projeto_base_de_telas_e_login.service.Produto.ProdutoUseCase;
import projeto_base_de_telas_e_login.model.categoria.Categoria;
import projeto_base_de_telas_e_login.repository.CategoriaPorta;
import projeto_base_de_telas_e_login.dto.Pedido.PedidoAddDTO;
import java.util.List;

@RestController
@RequestMapping("/productsPublico")
public class TelaInicial {

    private final ProdutoUseCase produtoUseCase;
    private final PedidoUserCase pedidoUserCase;
    private final CategoriaPorta categoriaPorta;
    private final EstoqueUseCase useCase;

    public TelaInicial(ProdutoUseCase produtoUseCase, PedidoUserCase pedidoUserCase,CategoriaPorta categoriaPorta,EstoqueUseCase useCase) {
        this.produtoUseCase = produtoUseCase;
        this.pedidoUserCase = pedidoUserCase;
        this.categoriaPorta = categoriaPorta;
        this.useCase = useCase;
    }

    @Operation(summary = "Lista todos os produtos")
    @GetMapping("/list")
    public ResponseEntity<List<ProductListaDto>> listarProdutos() {
        return ResponseEntity.ok(
                produtoUseCase.findAll()
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
                produtoUseCase.findByCategoriaNome(nome)
                        .stream()
                        .map(ProductListaDto::fromDomain)
                        .toList()
        );
    }

    @Operation(summary = "Lista todas as categorias")
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(
                categoriaPorta.findAll()
                        .stream()
                        .map(Categoria::getNomeCategoria) // <--- aqui estava errado
                        .toList()
        );
    }

    @PostMapping("/pedidos")
    public ResponseEntity<Void> criarPedido(@RequestBody PedidoAddDTO dto) {
        pedidoUserCase.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping("/lista")
//    public ResponseEntity<?> mostrarProdutosMenorPreco()  {
//        return ResponseEntity.ok(useCase.mostrarProdutosMenorPreco());
//    }


    // 🔎 GET - listar todos
    @GetMapping("/lista")
    public ResponseEntity<?> listar()  {
        return ResponseEntity.ok(useCase.listarTodos());
    }


}

