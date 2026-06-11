package projeto_base_de_telas_e_login.Pedido.Controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.Pedido.Controller.api.PedidoApi;
import projeto_base_de_telas_e_login.Pedido.PedidoService;
import projeto_base_de_telas_e_login.Pedido.dto.AtualizarStatusPedidoDTO;

import projeto_base_de_telas_e_login.Pedido.dto.DetalhePedidoDTO;

import projeto_base_de_telas_e_login.Pedido.dto.ListaDePedidoDTO;
import projeto_base_de_telas_e_login.Pedido.dto.PedidoAddDTO;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class TelaPedido implements PedidoApi {

    private final PedidoService service;

    public TelaPedido(PedidoService service) {
        this.service = service;
    }

    public ResponseEntity<List<ListaDePedidoDTO>> listaDePedidoDoDia() {
        return ResponseEntity.ok(service.listarPedidosDoDia().stream().map(ListaDePedidoDTO::fromEntity).toList());
    }

    public ResponseEntity<List<ListaDePedidoDTO>> todosOsPedidos(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable).stream().map(ListaDePedidoDTO::fromEntity).toList());
    }

    public ResponseEntity<byte[]> imprimirPDF(@PathVariable Long id) {
        byte[] pdf = service.imprimirPDF(id);
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=pedido-" + id + ".pdf").body(pdf);
    }

    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO dto) {
        service.atualizarStatusPedido(id, dto.status());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<DetalhePedidoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(DetalhePedidoDTO.fromEntity(service.buscarPorId(id)));
    }
    public ResponseEntity<Void> criarPedido(@RequestBody PedidoAddDTO dto) {
        service.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}