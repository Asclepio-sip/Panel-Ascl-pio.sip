package Asclepio.MovimentacaoEstoque.Controller.Api;

import Asclepio.Estoque.dto.EstoqueFiltro;
import Asclepio.Estoque.dto.ListaDeEstoqueDasLojasResponse;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import Asclepio.Produto.dto.PageResponse;
import Asclepio.Produto.dto.ProdutoFiltro;
import Asclepio.Produto.dto.ProdutoStorageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;

@Tag(name = "Movimentações de Estoque", description = """
        Consulta do histórico de movimentações do estoque.
        
        Permite consultar alterações como:
        - criação de estoque
        - atualização manual
        - aplicação ou remoção de promoção
        - baixa automática por pedido
        - exclusão de estoque
        
        Todos os filtros são opcionais.
        """)
@RequestMapping("/movimentacoes-estoque")
public interface MovimentacaoEstoqueApi {

    @Operation(summary = "Listar movimentações de estoque", description = """
            Retorna uma lista paginada das movimentações de estoque.
            
            Exemplos de uso:
            
            /movimentacoes-estoque?lojaId=1
            
            /movimentacoes-estoque?produtoId=5
            
            /movimentacoes-estoque?tipo=SAIDA_PEDIDO
            
            /movimentacoes-estoque?usuario=mateus
            
            /movimentacoes-estoque?dataInicio=2026-06-01&dataFim=2026-06-12
            
            /movimentacoes-estoque?nomeProduto=dipirona&sort=criadoEm,desc
            
            Filtros disponíveis:
            - lojaId: filtra por ID da loja
            - produtoId: filtra por ID do produto
            - estoqueId: filtra por ID do estoque
            - nomeLoja: busca parcial pelo nome da loja
            - nomeProduto: busca parcial pelo nome do produto
            - usuario: busca parcial pelo nome do usuário
            - tipo: CRIACAO, ATUALIZACAO, PROMOCAO, SAIDA_PEDIDO ou DELECAO
            - dataInicio: data inicial no formato yyyy-MM-dd
            - dataFim: data final no formato yyyy-MM-dd
            
            Paginação:
            - page: número da página
            - size: quantidade de registros por página
            - sort: campo e direção. Exemplo: criadoEm,desc
            """)
    @GetMapping
    @PreAuthorize("hasAuthority('VerMovimentacaoEstoque')")
    ResponseEntity<Page<MovimentacaoEstoqueResponse>> listar(@ParameterObject MovimentacaoEstoqueFiltro filtro,
                                                             @ParameterObject @Parameter(description = """
                                                                     Paginação e ordenação.
                                                                     
                                                                     Exemplos:
                                                                     page=0&size=10
                                                                     page=0&size=20&sort=criadoEm,desc
                                                                     """) Pageable pageable);


    @GetMapping("/loja")
    @PreAuthorize("hasAuthority('VerMovimentacaoEstoque')")
    @Operation(summary = "Listar lojas com filtros", description = """
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
            """)
    ResponseEntity<Page<LojaResponse>> listar(@ParameterObject LojaFiltroDTO filtro, @ParameterObject Pageable pageable);



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
    @GetMapping("/relatorio")
    @PreAuthorize("hasAuthority('VerMovimentacaoEstoque')")
    ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(
            @ParameterObject EstoqueFiltro filtro,
            @ParameterObject Pageable pageable
    );


    @Operation(summary = "Listar produtos", description = """
            Lista os produtos com paginação e filtros opcionais.
            
            Filtros disponíveis:
            - nome: filtra pelo nome do produto
            - categoriaId: filtra pelo ID da categoria
            - nomeCategoria: filtra pelo nome da categoria
            
            Exemplos:
            
            Listar todos:
            /produtos?page=0&size=10
            
            Buscar por nome:
            /produtos?nome=dipirona&page=0&size=10
            
            Buscar por categoria:
            /produtos?categoriaId=1&page=0&size=10
            
            Buscar por nome da categoria:
            /produtos?nomeCategoria=medicamento&page=0&size=10
            
            Buscar por nome e categoria:
            /produtos?nome=dipirona&categoriaId=1&page=0&size=10
            
            Ordenar por nome:
            /produtos?page=0&size=10&sort=name,asc
            """)
    @GetMapping("/produtos")
    @PreAuthorize("hasAuthority('VerMovimentacaoEstoque')")
    ResponseEntity<PageResponse<ProdutoStorageResponse>> listar(@ParameterObject ProdutoFiltro filtro, @ParameterObject Pageable pageable);


}