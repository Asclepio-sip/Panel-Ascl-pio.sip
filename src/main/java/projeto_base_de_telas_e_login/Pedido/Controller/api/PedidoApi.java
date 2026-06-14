package projeto_base_de_telas_e_login.Pedido.Controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Pedido.dto.*;

import java.util.List;

@RequestMapping("/pedidos")
@Tag(name = "Pedido")
public interface PedidoApi {


    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAuthority('VerPedido')")
    public ResponseEntity<byte[]> imprimirPDF(@PathVariable Long id);

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('EditarPedido')")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO dto);

    @Operation(summary = "Criar pedido")
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoCriadoResponseDTO> criarPedido(@RequestBody PedidoAddDTO dto);



    @GetMapping
    @PreAuthorize("hasAuthority('VerPedido')")
    @Operation(
            summary = "Listar pedidos com filtros",
            description = """
                Lista pedidos de forma paginada com filtros opcionais.

                Exemplos:

                /pedidos?page=0&size=10&sort=criadoEm,desc

                /pedidos?lojaId=1

                /pedidos?nomeCliente=mateus

                /pedidos?telefone=81999999999

                /pedidos?status=AGUARDANDO

                /pedidos?tipoEntrega=ENTREGA

                /pedidos?formaDePagamento=PIX

                /pedidos?dataInicio=2026-06-01&dataFim=2026-06-13

                /pedidos?bairro=Centro&freteGratis=true
                """
    )
    ResponseEntity<Page<ListaDePedidoDTO>> listar(
            @ParameterObject PedidoFiltro filtro,
            @ParameterObject Pageable pageable
    );



    @GetMapping("/status/{codigoRastreio}")
    public ResponseEntity<PedidoStatusResponseDTO> consultarStatus(@PathVariable String codigoRastreio);

}
