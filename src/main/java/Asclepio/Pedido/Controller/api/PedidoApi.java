package Asclepio.Pedido.Controller.api;

import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
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
    @Operation(
            summary = "Listar pedidos",
            description = """

Lista os pedidos da empresa do usuário autenticado de forma paginada.

A empresa é identificada automaticamente pelo token JWT, portanto não é necessário informar empresaId.

Filtros disponíveis:

• lojaId
• nomeLoja
• nomeCliente
• telefone
• email
• bairro
• status
• tipoAtendimentoPedido
• formaDePagamento
• freteGratis
• dataInicio
• dataFim
• somenteHoje

Exemplos:

/pedidos?page=0&size=10&sort=criadoEm,desc

/pedidos?lojaId=1

/pedidos?nomeCliente=Mateus

/pedidos?status=AGUARDANDO

/pedidos?tipoAtendimentoPedido=ENTREGA_PROPRIA

/pedidos?formaDePagamento=PIX

/pedidos?bairro=Centro

/pedidos?freteGratis=true

/pedidos?somenteHoje=true

/pedidos?dataInicio=2026-06-01&dataFim=2026-06-30

"""
    )
    ResponseEntity<Page<ListaDePedidoDTO>> listar(@ParameterObject PedidoFiltro filtro, @ParameterObject Pageable pageable);


    @GetMapping("/status/{codigoRastreio}")
    @Operation(summary = "Consultar status por código de rastreio", description = "Consulta pública do status do pedido usando o código de rastreio.")
    ResponseEntity<PedidoStatusResponseDTO> consultarStatus(@PathVariable String codigoRastreio);

    @Operation(summary = "Criar pedido de balcão", description = "Cria um pedido presencial/balcão para a empresa do usuário autenticado.")
    @PostMapping("/balcao")
    @PreAuthorize("hasAuthority('CriarPedido')")
    ResponseEntity<PedidoCriadoResponseDTO> criarPedidoBalcao(@RequestBody PedidoBalcaoAddDTO dto);



    @GetMapping("/loja")
    @PreAuthorize("hasAuthority('CriarPedido')")
    @Operation(
            summary = "Listar lojas com filtros",
            description = """
                    Lista lojas de forma paginada.
                    
                    Filtros disponíveis:
                    - id: busca pelo ID da loja.
                    - nomeLoja: busca parcial pelo nome.
                    - cep: busca parcial pelo CEP.
                    - cnpj: busca parcial pelo CNPJ.
                    - telefone: busca parcial pelo telefone.
                    - tipoAtendimento: filtra por ENTREGA, RETIRADA ou AMBOS.
                    
                    Paginação:
                    - page: número da página.
                    - size: quantidade por página.
                    - sort: campo de ordenação. Exemplo: nomeLoja,asc
                    """
    )
    ResponseEntity<Page<LojaResponse>> listar(

            @ParameterObject
            LojaFiltroDTO filtro,

            @ParameterObject
            Pageable pageable
    );

    @Operation(
            summary = "Relatório de estoque das lojas",
            description = """
                Lista o estoque das lojas com paginação e filtros opcionais.

                Filtros disponíveis via query params:
                - lojaId: filtra pelo ID da loja
                - nomeLoja: filtra pelo nome da loja
                - nomeProduto: filtra pelo nome do produto
                - nomeVariacao: filtra pela variação do produto
                - categoriaId: filtra pelo ID da categoria
                - nomeCategoria: filtra pelo nome da categoria
                - semEstoque: true para listar apenas itens com quantidade 0

                Exemplos:
                /estoques/relatorio?page=0&size=10
                /estoques/relatorio?categoriaId=1&page=0&size=10
                /estoques/relatorio?nomeCategoria=medicamento&page=0&size=10
                /estoques/relatorio?lojaId=2&categoriaId=1&page=0&size=10
                /estoques/relatorio?nomeProduto=dipirona&semEstoque=true&page=0&size=10
                /estoques/relatorio?page=0&size=10&sort=precoVenda,asc
                """
    )
    @GetMapping("/estoque")
    @PreAuthorize("hasAuthority('VerPedido')")
    ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(
            @ParameterObject EstoqueFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalhes do pedido")
    @PreAuthorize("hasAuthority('VerPedido')")
    ResponseEntity<DetalhePedidoDTO> buscarPorId(
            @PathVariable Long id
    );
}
