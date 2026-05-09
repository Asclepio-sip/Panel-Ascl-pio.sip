package projeto_base_de_telas_e_login.adapter.in.web.controllers.telaPedido;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.AtualizarStatusPedidoDTO;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.DetalhePedidoDTO;
import projeto_base_de_telas_e_login.adapter.in.web.dto.Pedido.ListaDePedidoDTO;
import projeto_base_de_telas_e_login.domain.UseCase.Pedido.PedidoUserCase;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class TelaPedido {
    private final PedidoUserCase pedidoUserCase;

    public TelaPedido(
            PedidoUserCase pedidoUserCase
    ){
        this.pedidoUserCase = pedidoUserCase;
    }

    @GetMapping
    public ResponseEntity<List<ListaDePedidoDTO>> listaDePedidoDoDia(){
        return ResponseEntity.ok(pedidoUserCase.listarTodos()
                .stream()
                .map(ListaDePedidoDTO::fromDomain)
                .toList());
    }

    @GetMapping("/todosPedido")
    public ResponseEntity<List<ListaDePedidoDTO>> todosOsPedidos(){
        return ResponseEntity.ok(pedidoUserCase.listarTodos()
                .stream()
                .map(ListaDePedidoDTO::fromDomain)
                .toList());
    }

    @GetMapping(value = "/{id}/pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> imprimirPDF(@PathVariable Long id){

        byte[] pdf = pedidoUserCase.imprimirPDF(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=pedido-" + id + ".pdf")
                .body(pdf);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusPedidoDTO dto
    ) {

        pedidoUserCase.atualizarStatusPedido(id, dto.getStatus());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhePedidoDTO> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(
                DetalhePedidoDTO.fromDomain(
                        pedidoUserCase.buscarPorId(id)
                )
        );
    }
}
