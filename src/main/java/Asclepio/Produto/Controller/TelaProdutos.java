package Asclepio.Produto.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.Produto.Controller.api.ProdutoApi;
import Asclepio.Produto.Product;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.ProductoAddDto;
import Asclepio.Produto.dto.ProductoResponseDto;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
public class TelaProdutos implements ProdutoApi {

    private final ProdutoService produtoService;

    public TelaProdutos(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public ResponseEntity<Page<ProductoResponseDto>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodos(filtro, pageable));
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