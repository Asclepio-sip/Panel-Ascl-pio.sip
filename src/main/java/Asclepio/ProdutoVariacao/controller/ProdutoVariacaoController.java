package Asclepio.ProdutoVariacao.controller;

import Asclepio.Categoria.CategoriaService;
import Asclepio.Categoria.dto.CategoriaFiltro;
import Asclepio.Categoria.dto.CategoriaPageResponse;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoPageResponse;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.controller.api.ProdutoVariacaoApi;
import Asclepio.ProdutoVariacao.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoVariacaoController implements ProdutoVariacaoApi {

    private final ProdutoVariacaoService service;
    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;


    public ProdutoVariacaoController(ProdutoVariacaoService service, CategoriaService categoriaService,ProdutoService produtoService) {
        this.service = service;
        this.categoriaService = categoriaService;
        this.produtoService= produtoService;
    }

    @Override
    public ResponseEntity<ProdutoVariacaoPageResponse> listar(ProdutoVariacaoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponseDTO> criar(Long produtoId, ProdutoVariacaoAddDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(produtoId, dto));
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(Long id, ProdutoVariacaoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoriaPageResponse> listar(CategoriaFiltro filtro, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodosStorage(filtro, pageable));
    }

//
//    @Override
//    public ResponseEntity<CategoriaPageResponse> listar(CategoriaFiltro filtro, Pageable pageable) {
//        return ResponseEntity.ok(categoriaService.listar(filtro, pageable));
//    }
}