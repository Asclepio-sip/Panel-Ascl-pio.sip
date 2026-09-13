package Asclepio.Produto.Controller;

import Asclepio.CadastroProduto.CadastroProdutoService.CadastroProdutoService;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoDTO;
import Asclepio.CadastroProduto.dto.CadastroProdutoCompletoResponse;
import Asclepio.Categoria.CategoriaService;
import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Categoria.dto.CategoriaResponse;
import Asclepio.Categoria.dto.CriarCategoria;
import Asclepio.Produto.Controller.api.ProdutoApi;
import Asclepio.Produto.Produto;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoResponse;
import Asclepio.Produto.dto.ProdutoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TelaProdutos implements ProdutoApi {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final CadastroProdutoService cadastroProdutoService;

    public TelaProdutos(
            ProdutoService produtoService,
            CategoriaService categoriaService,
            CadastroProdutoService cadastroProdutoService
    ) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.cadastroProdutoService = cadastroProdutoService;
    }

    @Override
    public ResponseEntity<CategoriaPageResponse> listar(CategoriaFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(categoriaService.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<Page<ProdutoResponse>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodos(filtro, pageable));
    }

    @Override
    public ResponseEntity<ProdutoResponse> buscarPorId(Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }

    @Override
    public ResponseEntity<ProdutoResponse> atualizarImagem(Long id, MultipartFile imagem) {
        Produto produto = produtoService.atualizarImagem(id, imagem);
        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }

    @Override
    public ResponseEntity<ProdutoResponse> criar(
            String nome,
            String descricao,
            String marca,
            Long categoriaId,
            MultipartFile imagem
    ) {
        Produto produto = produtoService.criarComImagem(nome, descricao, marca, categoriaId, imagem);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProdutoResponse.fromEntity(produto));
    }

    @Override
    public ResponseEntity<ProdutoResponse> editar(Long id, ProdutoUpdateRequest dto) {
        Produto produto = produtoService.editar(id, dto);
        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoriaResponse> criar(CriarCategoria dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaService.criar(dto));
    }

    @Override
    public ResponseEntity<CadastroProdutoCompletoResponse> cadastrar(CadastroProdutoCompletoDTO dto) {
        CadastroProdutoCompletoResponse response = cadastroProdutoService.cadastrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}