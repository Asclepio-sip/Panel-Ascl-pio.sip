package Asclepio.Estoque.controller.api;

import Asclepio.Estoque.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.Estoque.dto.*;

@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Gerenciamento de estoque")
public interface EstoqueApi {

    @Operation(summary = "Criar item estoque")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso")
    @PostMapping
    @PreAuthorize("hasAuthority('CriarEstoque')")
    ResponseEntity<Void> criar(@RequestBody @Valid EstoqueAddDto dto);

    @Operation(summary = "Atualizar estoque")
    @ApiResponse(responseCode = "200", description = "Atualizado")
    @PatchMapping
    @PreAuthorize("hasAuthority('EditarEstoque')")
    ResponseEntity<Void> atualizar(@RequestBody @Valid AtulizarResquet dto);

    @Operation(summary = "Aplicar promoção")
    @ApiResponse(responseCode = "200", description = "Promoção aplicada")
    @PatchMapping("/promocao")
    @PreAuthorize("hasAuthority('PromocaoEstoque')")
    ResponseEntity<Void> aplicarPromocao(@RequestBody @Valid AplicarPromocaoDto dto);

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
    ResponseEntity<Page<ListaDeEstoqueDasLojasResponse>> lista(
            @ParameterObject EstoqueFiltro filtro,
            @ParameterObject Pageable pageable
    );
    @Operation(summary = "Deletar item estoque")
    @ApiResponse(responseCode = "204", description = "Deletado")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirEstoque')")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}