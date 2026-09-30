package Asclepio.CatalogoOnline.controller.api;

import Asclepio.CatalogoOnline.dto.CatalogoCategoriaResponse;
import Asclepio.CatalogoOnline.dto.CatalogoCheckoutResponse;
import Asclepio.CatalogoOnline.dto.CatalogoLojaResponse;
import Asclepio.CatalogoOnline.dto.CatalogoOnlineFiltro;
import Asclepio.CatalogoOnline.dto.CatalogoPedidoRequest;
import Asclepio.CatalogoOnline.dto.CatalogoPedidoResponse;
import Asclepio.CatalogoOnline.dto.CatalogoProdutoResponse;
import Asclepio.CatalogoOnline.dto.CatalogoResponse;
import Asclepio.ConfigConta.dto.DesignCatalogoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/catalogo-online")
@Tag(name = "Catálogo online", description = "Catálogo público de produtos (sem login)")
public interface CatalogoOnlineApi {

    @Operation(summary = "Abrir catálogo", description = """
            Rota PÚBLICA (não precisa de token).
            
            Tudo o que a página inicial do catálogo precisa em uma chamada só:
            nome da empresa, design (card, navbar, cores, logo, banner, rodapé, redes),
            lojas e categorias ativas. Os produtos vêm de /{nomeLink}/lojas/{lojaId}/produtos.
            
            Nome do link inexistente ou catálogo desativado → 404.
            
            Exemplo:
            /catalogo-online/farmacia-central
            """)
    @ApiResponse(responseCode = "200", description = "Dados do catálogo")
    @ApiResponse(responseCode = "404", description = "Nome do link não encontrado ou catálogo desativado")
    @GetMapping("/{nomeLink}")
    ResponseEntity<CatalogoResponse> buscarCatalogo(@PathVariable String nomeLink);

    @Operation(summary = "Listar categorias do catálogo", description = """
            Rota PÚBLICA (não precisa de token).
            
            Categorias ativas da empresa do {nomeLink}, em ordem de nome,
            para a navbar e o filtro de produtos (use o id em categoriaId).
            
            Nome do link inexistente ou catálogo desativado → 404.
            
            Exemplo:
            /catalogo-online/farmacia-central/categorias
            """)
    @ApiResponse(responseCode = "200", description = "Categorias ativas")
    @ApiResponse(responseCode = "404", description = "Nome do link não encontrado ou catálogo desativado")
    @GetMapping("/{nomeLink}/categorias")
    ResponseEntity<List<CatalogoCategoriaResponse>> listarCategorias(@PathVariable String nomeLink);

    @Operation(summary = "Buscar design do catálogo", description = """
            Rota PÚBLICA (não precisa de token).
            
            Modelo de card, cores, descrição do rodapé, contatos/redes sociais,
            botão flutuante do WhatsApp, logo e banner da empresa, para o site público montar o layout.
            logoUrl / bannerUrl = null quando não há imagem ativa.
            
            Nome do link inexistente ou catálogo desativado → 404.
            
            Exemplo:
            /catalogo-online/farmacia-central/design
            """)
    @ApiResponse(responseCode = "200", description = "Design do catálogo")
    @ApiResponse(responseCode = "404", description = "Nome do link não encontrado ou catálogo desativado")
    @GetMapping("/{nomeLink}/design")
    ResponseEntity<DesignCatalogoResponse> buscarDesign(@PathVariable String nomeLink);

    @Operation(summary = "Listar lojas do catálogo", description = """
            Rota PÚBLICA (não precisa de token).
            
            Lista as lojas da empresa dona do {nomeLink}, em ordem de nome,
            para o cliente escolher de qual loja quer ver os produtos.
            
            Nome do link inexistente ou catálogo desativado → 404.
            
            Exemplo:
            /catalogo-online/farmacia-central/lojas
            """)
    @ApiResponse(responseCode = "200", description = "Lojas da empresa")
    @ApiResponse(responseCode = "404", description = "Nome do link não encontrado ou catálogo desativado")
    @GetMapping("/{nomeLink}/lojas")
    ResponseEntity<List<CatalogoLojaResponse>> listarLojas(@PathVariable String nomeLink);

    @Operation(summary = "Listar produtos do catálogo da loja", description = """
            Rota PÚBLICA (não precisa de token).
            
            {nomeLink} = nome definido em POST/PUT /config-conta/nome-link.
            Só funciona se essa empresa estiver com o catálogo online ativado
            em PATCH /config-conta/catalogo-online, e se a loja for dela. Senão → 404.
            
            Lista apenas itens com quantidade > 0 e variação ativa.
            
            Filtros disponíveis:
            - nomeProduto: busca parcial pelo nome do produto
            - categoriaId: filtra pela categoria
            - somentePromocao: true para listar só itens com desconto
            
            Exemplos:
            /catalogo-online/farmacia-central/lojas/1/produtos?page=0&size=20
            /catalogo-online/farmacia-central/lojas/1/produtos?nomeProduto=dipirona
            /catalogo-online/farmacia-central/lojas/1/produtos?somentePromocao=true&sort=precoVenda,asc
            """)
    @ApiResponse(responseCode = "200", description = "Produtos do catálogo")
    @ApiResponse(responseCode = "404", description = "Nome do link/loja não encontrados ou catálogo desativado")
    @GetMapping("/{nomeLink}/lojas/{lojaId}/produtos")
    ResponseEntity<Page<CatalogoProdutoResponse>> listarProdutos(
            @PathVariable String nomeLink,
            @PathVariable Long lojaId,
            @ParameterObject CatalogoOnlineFiltro filtro,
            @ParameterObject Pageable pageable);

    @Operation(summary = "Listar formas de pagamento da loja", description = """
            Rota PÚBLICA (não precisa de token).
            
            Formas de pagamento ativas na loja {lojaId}. Só essas são aceitas
            em formaDePagamento no pedido pelo catálogo.
            
            Nome do link/loja não encontrados ou catálogo desativado → 404.
            
            Exemplo:
            /catalogo-online/farmacia-central/lojas/1/formas-pagamento
            """)
    @ApiResponse(responseCode = "200", description = "Formas de pagamento ativas")
    @ApiResponse(responseCode = "404", description = "Nome do link/loja não encontrados ou catálogo desativado")
    @GetMapping("/{nomeLink}/lojas/{lojaId}/formas-pagamento")
    ResponseEntity<List<CatalogoCheckoutResponse.FormaPagamento>> listarFormasPagamento(@PathVariable String nomeLink, @PathVariable Long lojaId);

    @Operation(summary = "Opções de checkout da loja", description = """
            Rota PÚBLICA (não precisa de token).
            
            O que o cliente pode escolher ao finalizar o pedido na loja {lojaId}:
            - tiposAtendimento: RETIRADA_NA_LOJA e/ou ENTREGA_PROPRIA, conforme a loja;
            - formasPagamento: só as ativas na loja;
            - bairros atendidos com o valor do frete (vazio se a loja não entrega);
            - valorMinimoFreteGratis: a partir desse subtotal o frete é zero (null = sem frete grátis).
            
            Nome do link/loja não encontrados ou catálogo desativado → 404.
            
            Exemplo:
            /catalogo-online/farmacia-central/lojas/1/checkout
            """)
    @ApiResponse(responseCode = "200", description = "Opções de checkout")
    @ApiResponse(responseCode = "404", description = "Nome do link/loja não encontrados ou catálogo desativado")
    @GetMapping("/{nomeLink}/lojas/{lojaId}/checkout")
    ResponseEntity<CatalogoCheckoutResponse> buscarCheckout(@PathVariable String nomeLink, @PathVariable Long lojaId);

    @Operation(summary = "Fazer pedido pelo catálogo", description = """
            Rota PÚBLICA (não precisa de token).
            
            O cliente faz o pedido na loja {lojaId} do catálogo {nomeLink}.
            Usa o mesmo fluxo do pedido online: valida forma de pagamento e atendimento da loja,
            baixa o estoque e gera o código de rastreio.
            
            Regras extras do catálogo:
            - catálogo precisa estar ativo e a loja ser da empresa do {nomeLink} (senão 404);
            - tipoAtendimentoPedido não pode ser BALCAO;
            - cada variação só pode aparecer uma vez e precisa estar ativa.
            
            O status pode ser acompanhado em GET /pedidos/status/{codigoRastreio} (público).
            
            Exemplo de body:
            {
              "bairroId": 3,
              "nomeCliente": "Maria",
              "email": "maria@email.com",
              "telefone": "11999998888",
              "endereco": "Rua A, 100",
              "complemento": "Apto 12",
              "observacao": "Tocar o interfone",
              "tipoAtendimentoPedido": "ENTREGA_PROPRIA",
              "formaDePagamento": "PIX",
              "itens": [ { "variacaoId": 7, "quantidade": 2 } ]
            }
            """)
    @ApiResponse(responseCode = "201", description = "Pedido criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos, estoque insuficiente ou produto indisponível")
    @ApiResponse(responseCode = "404", description = "Nome do link/loja não encontrados ou catálogo desativado")
    @PostMapping("/{nomeLink}/lojas/{lojaId}/pedidos")
    ResponseEntity<CatalogoPedidoResponse> criarPedido(
            @PathVariable String nomeLink,
            @PathVariable Long lojaId,
            @RequestBody CatalogoPedidoRequest dto);
}
