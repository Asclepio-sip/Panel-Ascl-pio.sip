package Asclepio.Pedido.Controller.api;

import Asclepio.Pedido.dto.*;
import Asclepio.Pedido.dto.pedido.PedidoAddDTO;
import Asclepio.Pedido.dto.pedido.PedidoBalcaoAddDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pedidos")
@Tag(name = "Pedido")
public interface PedidoApi {


    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAuthority('VerPedido')")
    @Operation(summary = "Gerar PDF do pedido", description = "Gera o PDF de um pedido da empresa do usuário autenticado.")
    ResponseEntity<byte[]> imprimirPDF(@PathVariable Long id);

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('EditarPedido')")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido pertencente à empresa do usuário autenticado.")
    ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO dto);


    @Operation(summary = "Criar pedido online", description = "Cria um pedido online a partir da loja informada. A empresa do pedido é definida automaticamente pela loja.")
    @PostMapping
    ResponseEntity<PedidoCriadoResponseDTO> criarPedido(@RequestBody PedidoAddDTO dto);


    @GetMapping
    @PreAuthorize("hasAuthority('VerPedido')")
    @Operation(summary = "Listar pedidos com filtros", description = """
            
               Lista pedidos de forma paginada com filtros opcionais.
                        Retorna apenas pedidos da empresa do usuário autenticado.
            
            
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
            """)
    ResponseEntity<Page<ListaDePedidoDTO>> listar(@ParameterObject PedidoFiltro filtro, @ParameterObject Pageable pageable);


    @GetMapping("/status/{codigoRastreio}")
    @Operation(summary = "Consultar status por código de rastreio", description = "Consulta pública do status do pedido usando o código de rastreio.")
    ResponseEntity<PedidoStatusResponseDTO> consultarStatus(@PathVariable String codigoRastreio);

    @Operation(summary = "Criar pedido de balcão", description = "Cria um pedido presencial/balcão para a empresa do usuário autenticado.")
    @PostMapping("/balcao")
    @PreAuthorize("hasAuthority('CriarPedido')")
    ResponseEntity<PedidoCriadoResponseDTO> criarPedidoBalcao(@RequestBody PedidoBalcaoAddDTO dto);

}
