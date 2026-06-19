package Asclepio.MovimentacaoEstoque.Controller.Api;

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
    @PreAuthorize("hasAuthority('VerEstoque')")
    ResponseEntity<Page<MovimentacaoEstoqueResponse>> listar(@ParameterObject MovimentacaoEstoqueFiltro filtro,

                                                             @ParameterObject @Parameter(description = """
                                                                     Paginação e ordenação.
                                                                     
                                                                     Exemplos:
                                                                     page=0&size=10
                                                                     page=0&size=20&sort=criadoEm,desc
                                                                     """) Pageable pageable);
}