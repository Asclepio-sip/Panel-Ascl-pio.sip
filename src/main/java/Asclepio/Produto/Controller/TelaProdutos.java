package Asclepio.Produto.Controller;

import Asclepio.Produto.*;
import Asclepio.Produto.dto.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.Produto.Controller.api.ProdutoApi;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TelaProdutos implements ProdutoApi {

    private final ProdutoService produtoService;
    private final ProdutoStorageClient produtoStorageClient;


    public TelaProdutos(ProdutoService produtoService, ProdutoStorageClient produtoStorageClient) {
        this.produtoService = produtoService;
        this.produtoStorageClient = produtoStorageClient;
    }

    @Override
    public ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodosStorage(filtro, pageable));
    }

    @Override
    public ResponseEntity<ProdutoStorageResponse> criar(String nome, String descricao, String marca, Long categoriaId, MultipartFile imagem) {
        ProdutoStorageResponse produto = produtoService.criarComStorage(nome, descricao, marca, categoriaId, imagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @Override
    public ResponseEntity<ProdutoStorageResponse> editar(Long id, ProdutoUpdateDto dto) {

        ProdutoStorageResponse produto = produtoService.atualizarComStorage(id, dto);

        return ResponseEntity.ok(produto);
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        produtoService.deletarComStorage(id);
        return ResponseEntity.noContent().build();
    }
}