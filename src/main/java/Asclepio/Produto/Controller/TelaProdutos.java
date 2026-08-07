package Asclepio.Produto.Controller;

import Asclepio.CadastroProduto.CadastroProdutoService.CadastroProdutoService;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import Asclepio.Categoria.CategoriaService;
import Asclepio.Categoria.CategoriaStorageClient;
import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.Produto.*;
import Asclepio.Produto.Controller.api.ProdutoApi;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.Produto.dto.ProdutoUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

@RestController
public class TelaProdutos implements ProdutoApi {

    private final ProdutoService produtoService;
    private final ProdutoStorageClient produtoStorageClient;
    private final CategoriaService categoriaService;
    private final CadastroProdutoService cadastroProdutoService;

    public TelaProdutos(
            ProdutoService produtoService,
            ProdutoStorageClient produtoStorageClient,
            CategoriaService categoriaService,
            CadastroProdutoService cadastroProdutoService
    ) {
        this.produtoService = produtoService;
        this.produtoStorageClient = produtoStorageClient;
        this.categoriaService = categoriaService;
        this.cadastroProdutoService = cadastroProdutoService;

    }



    @Override
    public ResponseEntity<CategoriaPageResponse> listar(CategoriaFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(categoriaService.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(
            ProdutoFiltro filtro,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                produtoService.listarTodosStorage(filtro, pageable)
        );
    }

    @Override
    public ResponseEntity<ProdutoStorageResponse> criar(
            String nome,
            String descricao,
            String marca,
            Long categoriaId,
            MultipartFile imagem
    ) {

        ProdutoStorageResponse produto =
                produtoService.criarComStorage(
                        nome,
                        descricao,
                        marca,
                        categoriaId,
                        imagem
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produto);
    }

    @Override
    public ResponseEntity<ProdutoStorageResponse> editar(
            Long id,
            ProdutoUpdateDto dto
    ) {

        ProdutoStorageResponse produto =
                produtoService.atualizarComStorage(id, dto);

        return ResponseEntity.ok(produto);
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {

        produtoService.deletarComStorage(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoriaResponse> criar(CriarCategoria dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaService.criar(dto));
    }

    @Override
    public ResponseEntity<CadastroProdutoCompletoResponse> cadastrar(
            CadastroProdutoCompletoDTO dto
    ) {

        CadastroProdutoCompletoResponse response =
                cadastroProdutoService.cadastrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}