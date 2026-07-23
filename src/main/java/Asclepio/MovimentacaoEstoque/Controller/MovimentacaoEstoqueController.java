package Asclepio.MovimentacaoEstoque.Controller;

import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.Estoque.service.EstoqueQueryService;
import Asclepio.Loja.Loja.LojaService;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.MovimentacaoEstoque.Controller.Api.MovimentacaoEstoqueApi;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoqueService;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;

@RestController
public class MovimentacaoEstoqueController implements MovimentacaoEstoqueApi {

    private final MovimentacaoEstoqueService service;
    private final LojaService lojaService;
    private final EstoqueQueryService estoqueQueryService;
    private final ProdutoService produtoService;



    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service,LojaService lojaService,EstoqueQueryService estoqueQueryService,ProdutoService produtoService) {
        this.service = service;
        this.lojaService = lojaService;
        this.estoqueQueryService = estoqueQueryService;
        this.produtoService = produtoService;
    }

    @Override
    public ResponseEntity<Page<MovimentacaoEstoqueResponse>> listar(MovimentacaoEstoqueFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<Page<LojaResponse>> listar(LojaFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(lojaService.listar(filtro, pageable));
    }


    @Override
    public ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(EstoqueFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(estoqueQueryService.listarTodos(filtro, pageable));
    }

    @Override
    public ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodosStorage(filtro, pageable));
    }


}