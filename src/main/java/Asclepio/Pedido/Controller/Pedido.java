package Asclepio.Pedido.Controller;

import Asclepio.Pedido.dto.*;
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
public class TelaPedido implements PedidoApi {

    private final PedidoService service;

    public TelaPedido(PedidoService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Page<ListaDePedidoDTO>> listar(PedidoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listarComFiltro(filtro, pageable));
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
}