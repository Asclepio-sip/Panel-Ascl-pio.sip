package projeto_base_de_telas_e_login.Pedido;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.Pedido.dto.AtualizarStatusPedidoDTO;

import projeto_base_de_telas_e_login.Pedido.dto.DetalhePedidoDTO;

import projeto_base_de_telas_e_login.Pedido.dto.ListaDePedidoDTO;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class TelaPedido {

    private final PedidoService service;

    public TelaPedido(PedidoService service) {

        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ListaDePedidoDTO>> listaDePedidoDoDia() {

        return ResponseEntity.ok(service.listarPedidosDoDia().stream().map(ListaDePedidoDTO::fromEntity).toList());
    }

    @GetMapping("/todosPedido")
    public ResponseEntity<List<ListaDePedidoDTO>> todosOsPedidos() {

        return ResponseEntity.ok(service.listarTodos().stream().map(ListaDePedidoDTO::fromEntity).toList());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> imprimirPDF(@PathVariable Long id) {

        byte[] pdf = service.imprimirPDF(id);

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=pedido-" + id + ".pdf").body(pdf);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO dto) {

        service.atualizarStatusPedido(id, dto.status());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhePedidoDTO> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(DetalhePedidoDTO.fromEntity(service.buscarPorId(id)));
    }
}