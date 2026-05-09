package projeto_base_de_telas_e_login.adapter.in.web.controllers.TelaProdutos;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Product.ProductoAddDto;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Product.ProductoResponseDto;
import projeto_base_de_telas_e_login.domain.UseCase.Produto.ProdutoUseCase;
import projeto_base_de_telas_e_login.domain.model.product.Product;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class TelaProdutos{

    private final ProdutoUseCase produtoUseCase;

    public TelaProdutos(ProdutoUseCase produtoUseCase) {
        this.produtoUseCase = produtoUseCase;
    }

    // Criar produto
    @PostMapping
    public ResponseEntity<ProductoResponseDto> criarProduto(
            @RequestBody @Valid ProductoAddDto dto
    ) {
        Product produto = dto.toDomain();
        produtoUseCase.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponseDto.fromDomain(produto));
    }

    // Atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> atualizarProduto(
            @PathVariable Long id,
            @RequestBody @Valid ProductoAddDto dto
    ) {
        Product produto = dto.toDomain();
        produto.setId(id); // define o ID para atualizar
        produtoUseCase.save(produto); // salvar faz update se o ID existir
        return ResponseEntity.ok(ProductoResponseDto.fromDomain(produto));
    }

    // Deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> listarTodos() {
        List<Product> produtos = produtoUseCase.findAll();
        List<ProductoResponseDto> dtoList = produtos.stream()
                .map(ProductoResponseDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Listar produtos por categoria
    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity<List<ProductoResponseDto>> listarPorCategoria(@PathVariable String nomeCategoria) {
        List<Product> produtos = produtoUseCase.findByCategoriaNome(nomeCategoria);
        List<ProductoResponseDto> dtoList = produtos.stream()
                .map(ProductoResponseDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Buscar produto por nome (adicional)
    @GetMapping("/search")
    public ResponseEntity<List<ProductoResponseDto>> buscarPorNome(@RequestParam String nome) {
        List<Product> produtos = produtoUseCase.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(nome.toLowerCase()))
                .toList();
        List<ProductoResponseDto> dtoList = produtos.stream()
                .map(ProductoResponseDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}