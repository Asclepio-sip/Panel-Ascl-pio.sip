package Asclepio.ProdutoVariacao.controller.api;

import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoPageResponse;
import Asclepio.ProdutoVariacao.dto.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/variacoes")
public interface ProdutoVariacaoApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoPageResponse> listar(
            @ParameterObject ProdutoVariacaoFiltro filtro,
            @ParameterObject Pageable pageable
    );


    @PostMapping("/produtos/{produtoId}")
    @PreAuthorize("hasAuthority('CriarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponseDTO> criar(
            @PathVariable Long produtoId,
            @RequestBody ProdutoVariacaoAddDTO dto
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarProdutoVariacao')")
    ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoVariacaoUpdateDTO dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirProdutoVariacao')")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}