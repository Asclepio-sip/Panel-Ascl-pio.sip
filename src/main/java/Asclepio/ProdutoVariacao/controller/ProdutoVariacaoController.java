package Asclepio.ProdutoVariacao.controller;

import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoPageResponse;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.controller.api.ProdutoVariacaoApi;
import Asclepio.ProdutoVariacao.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoVariacaoController implements ProdutoVariacaoApi {

    private final ProdutoVariacaoService service;

    public ProdutoVariacaoController(ProdutoVariacaoService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ProdutoVariacaoPageResponse> listar(
            ProdutoVariacaoFiltro filtro,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.listar(filtro, pageable)
        );
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponseDTO> criar(
            Long produtoId,
            ProdutoVariacaoAddDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.criar(produtoId, dto)
        );
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(
            Long id,
            ProdutoVariacaoUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                service.atualizar(id, dto)
        );
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}