package Asclepio.CatalogoOnline.controller;

import Asclepio.CatalogoOnline.CatalogoOnlineService;
import Asclepio.CatalogoOnline.controller.api.CatalogoOnlineApi;
import Asclepio.CatalogoOnline.dto.CatalogoCategoriaResponse;
import Asclepio.CatalogoOnline.dto.CatalogoCheckoutResponse;
import Asclepio.CatalogoOnline.dto.CatalogoLojaResponse;
import Asclepio.CatalogoOnline.dto.CatalogoOnlineFiltro;
import Asclepio.CatalogoOnline.dto.CatalogoPedidoRequest;
import Asclepio.CatalogoOnline.dto.CatalogoPedidoResponse;
import Asclepio.CatalogoOnline.dto.CatalogoProdutoResponse;
import Asclepio.CatalogoOnline.dto.CatalogoResponse;
import Asclepio.ConfigConta.dto.DesignCatalogoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogoOnlineController implements CatalogoOnlineApi {

    private final CatalogoOnlineService service;

    public CatalogoOnlineController(CatalogoOnlineService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<CatalogoResponse> buscarCatalogo(String nomeLink) {
        return ResponseEntity.ok(service.buscarCatalogo(nomeLink));
    }

    @Override
    public ResponseEntity<List<CatalogoCategoriaResponse>> listarCategorias(String nomeLink) {
        return ResponseEntity.ok(service.listarCategorias(nomeLink));
    }

    @Override
    public ResponseEntity<DesignCatalogoResponse> buscarDesign(String nomeLink) {
        return ResponseEntity.ok(service.buscarDesign(nomeLink));
    }

    @Override
    public ResponseEntity<List<CatalogoLojaResponse>> listarLojas(String nomeLink) {
        return ResponseEntity.ok(service.listarLojas(nomeLink));
    }

    @Override
    public ResponseEntity<Page<CatalogoProdutoResponse>> listarProdutos(String nomeLink, Long lojaId, CatalogoOnlineFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listarProdutos(nomeLink, lojaId, filtro, pageable));
    }

    @Override
    public ResponseEntity<List<CatalogoCheckoutResponse.FormaPagamento>> listarFormasPagamento(String nomeLink, Long lojaId) {
        return ResponseEntity.ok(service.listarFormasPagamento(nomeLink, lojaId));
    }

    @Override
    public ResponseEntity<CatalogoCheckoutResponse> buscarCheckout(String nomeLink, Long lojaId) {
        return ResponseEntity.ok(service.buscarCheckout(nomeLink, lojaId));
    }

    @Override
    public ResponseEntity<CatalogoPedidoResponse> criarPedido(String nomeLink, Long lojaId, CatalogoPedidoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarPedido(nomeLink, lojaId, dto));
    }
}
