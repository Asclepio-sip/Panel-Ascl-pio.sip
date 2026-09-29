package Asclepio.Estoque.controller;

import Asclepio.Estoque.controller.api.EstoqueApi;
import Asclepio.Estoque.EstoqueService;
import Asclepio.Estoque.dto.*;
import Asclepio.Estoque.service.EstoqueQueryService;
import Asclepio.Loja.Loja.LojaService;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import Asclepio.Produto.ProdutoService;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoResponse; // <-- Alterado de ProdutoStorageResponse
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelaEstoque implements EstoqueApi {

    private final EstoqueService service;
    private final EstoqueQueryService estoqueQueryService;
    private final LojaService lojaService;
    private final ProdutoService produtoService;

    public TelaEstoque(
            EstoqueService useCase,
            EstoqueQueryService estoqueQueryService,
            LojaService lojaService,
            ProdutoService produtoService
    ) {
        this.service = useCase;
        this.estoqueQueryService = estoqueQueryService;
        this.lojaService = lojaService;
        this.produtoService = produtoService;
    }

    @Override
    public ResponseEntity<Void> criar(EstoqueAddDto dto) {
        service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> atualizar(AtulizarResquet dto) {
        service.atualizar(dto.lojaID(), dto.produtoId(), dto.quantidade(), dto.precoVenda());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> aplicarPromocao(AplicarPromocaoDto dto) {
        service.aplicarPromocao(dto.lojaId(), dto.produtoId(), dto.percentual());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(EstoqueFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(estoqueQueryService.listarTodos(filtro, pageable));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<LojaResponse>> listar(LojaFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(lojaService.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<Page<LojaResponse>> listarEstoque(LojaFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(lojaService.listar(filtro, pageable));
    }

    // Método corrigido para usar listarTodos do ProdutoService local
    @Override
    public ResponseEntity<Page<ProdutoResponse>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodos(filtro, pageable));
    }
}