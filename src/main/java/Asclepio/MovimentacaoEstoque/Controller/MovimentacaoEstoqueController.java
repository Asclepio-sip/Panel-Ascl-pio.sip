package Asclepio.MovimentacaoEstoque.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import Asclepio.MovimentacaoEstoque.Controller.Api.MovimentacaoEstoqueApi;
import Asclepio.MovimentacaoEstoque.MovimentacaoEstoqueService;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueFiltro;
import Asclepio.MovimentacaoEstoque.dto.MovimentacaoEstoqueResponse;

@RestController
public class MovimentacaoEstoqueController implements MovimentacaoEstoqueApi {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Page<MovimentacaoEstoqueResponse>> listar(MovimentacaoEstoqueFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }
}