package Asclepio.Pedido.Controller;

import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.Estoque.service.EstoqueQueryService;
import Asclepio.Loja.Loja.LojaService;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import Asclepio.Pedido.Service.PedidoQueryService;
import Asclepio.Pedido.dto.*;
import Asclepio.Pedido.dto.pedido.PedidoAddDTO;
import Asclepio.Pedido.dto.pedido.PedidoBalcaoAddDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import Asclepio.Pedido.Controller.api.PedidoApi;
import Asclepio.Pedido.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class Pedido implements PedidoApi {

    private final PedidoService service;
    private final PedidoQueryService pedidoQueryService;
    private final LojaService lojaService;

    private final EstoqueQueryService estoqueQueryService;

    public Pedido(PedidoService service, PedidoQueryService pedidoQueryService,LojaService lojaService, EstoqueQueryService estoqueQueryService) {
        this.service = service;
        this.pedidoQueryService = pedidoQueryService;
        this.lojaService = lojaService;
        this.estoqueQueryService = estoqueQueryService;
    }

    @Override
    public ResponseEntity<Page<ListaDePedidoDTO>> listar(PedidoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(pedidoQueryService.listarComFiltro(filtro, pageable));
    }

    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO dto) {
        service.atualizarStatusPedido(id, dto.status());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<PedidoCriadoResponseDTO> criarPedido(@RequestBody PedidoAddDTO dto) {
        PedidoCriadoResponseDTO response = service.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<PedidoStatusResponseDTO> consultarStatus(@PathVariable String codigoRastreio) {
        return ResponseEntity.ok(service.consultarStatusPorCodigo(codigoRastreio));
    }

    @Override
    public ResponseEntity<byte[]> imprimirPDF(Long id) {

        byte[] pdf = service.imprimirPDF(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"pedido-" + id + ".pdf\"").body(pdf);
    }

    @Override
    public ResponseEntity<PedidoCriadoResponseDTO> criarPedidoBalcao(@RequestBody PedidoBalcaoAddDTO dto) {
        PedidoCriadoResponseDTO response = service.criarPedidoBalcao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Page<LojaResponse>> listar(LojaFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(lojaService.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(EstoqueFiltro filtro, Pageable pageable) {
        System.out.println("ENTROU NO MÉTODO");


        return ResponseEntity.ok(estoqueQueryService.listarTodos(filtro, pageable));
    }

    @Override
    public ResponseEntity<DetalhePedidoDTO> buscarPorId(Long id) {

        return ResponseEntity.ok(
                pedidoQueryService.buscarDetalhes(id)
        );
    }
}