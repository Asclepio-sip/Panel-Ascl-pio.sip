package projeto_base_de_telas_e_login.Produto.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import projeto_base_de_telas_e_login.Produto.Controller.api.ProdutoApi;
import projeto_base_de_telas_e_login.Produto.Product;
import projeto_base_de_telas_e_login.Produto.ProdutoService;
import projeto_base_de_telas_e_login.Produto.dto.ProductoAddDto;
import projeto_base_de_telas_e_login.Produto.dto.ProductoResponseDto;
import projeto_base_de_telas_e_login.Produto.dto.ProdutoUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
public class TelaProdutos implements ProdutoApi {

    private final ProdutoService produtoService;

    public TelaProdutos(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public ResponseEntity<Page<ProductoResponseDto>> listar(Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodos(pageable));
    }

    @Override
    public ResponseEntity<Product> criar(ProductoAddDto dto) {
        Product novo = produtoService.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @Override
    public ResponseEntity<ProductoResponseDto> editar(Long id, ProdutoUpdateDto product) {

        Product atualizado = produtoService.editar(id, product);

        return ResponseEntity.ok(ProductoResponseDto.fromEntity(atualizado));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {

        produtoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}