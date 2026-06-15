package Asclepio.ProdutoVariacao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.ProdutoVariacao.ProdutoVariacaoService;
import Asclepio.ProdutoVariacao.controller.api.ProdutoVariacaoApi;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoAddDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoResponseDTO;
import Asclepio.ProdutoVariacao.dto.ProdutoVariacaoUpdateDTO;

import java.util.List;

@RestController
public class ProdutoVariacaoController implements ProdutoVariacaoApi {

    private final ProdutoVariacaoService service;

    public ProdutoVariacaoController(ProdutoVariacaoService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponseDTO> criar(Long produtoId, ProdutoVariacaoAddDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(produtoId, dto));
    }

    @Override
    public ResponseEntity<List<ProdutoVariacaoResponseDTO>> listarPorProduto(Long produtoId) {
        return ResponseEntity.ok(service.listarPorProduto(produtoId));
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponseDTO> atualizar(Long produtoId, Long id, ProdutoVariacaoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Override
    public ResponseEntity<Void> deletar(Long produtoId, Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}