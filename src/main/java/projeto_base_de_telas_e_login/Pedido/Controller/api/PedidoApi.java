package projeto_base_de_telas_e_login.Pedido.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Pedido.dto.AtualizarStatusPedidoDTO;
import projeto_base_de_telas_e_login.Pedido.dto.DetalhePedidoDTO;
import projeto_base_de_telas_e_login.Pedido.dto.ListaDePedidoDTO;
import projeto_base_de_telas_e_login.Pedido.dto.PedidoAddDTO;

import java.util.List;

@RequestMapping("/pedidos")
@Tag(name = "Pedido")
public interface PedidoApi {


    @GetMapping
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public ResponseEntity<List<ListaDePedidoDTO>> listaDePedidoDoDia();

    @GetMapping("/todosPedido")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public ResponseEntity<List<ListaDePedidoDTO>> todosOsPedidos(Pageable pageable);

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public ResponseEntity<byte[]> imprimirPDF(@PathVariable Long id);

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('PEDIDO_UPDATE')")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO dto);

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PEDIDO_READ')")
    public ResponseEntity<DetalhePedidoDTO> buscarPorId(@PathVariable Long id);


    @Operation(summary = "Criar pedido")
    @PostMapping("/pedidos")
    public ResponseEntity<Void> criarPedido(@RequestBody PedidoAddDTO dto);

}
