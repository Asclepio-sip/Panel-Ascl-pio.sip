package Asclepio.Loja.LojaBairro.Controller;

import Asclepio.Loja.LojaBairro.Controller.api.LojaBairroApi;
import Asclepio.Loja.LojaBairro.LojaBairroService;
import Asclepio.Loja.LojaBairro.dto.CreateLojaBairroRequest;
import Asclepio.Loja.LojaBairro.dto.LojaBairroFiltroDTO;
import Asclepio.Loja.LojaBairro.dto.LojaBairroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class LojaBairroController implements LojaBairroApi {

    private final LojaBairroService service;

    public LojaBairroController(LojaBairroService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<LojaBairroResponse> criar(CreateLojaBairroRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(request));
    }

    @Override
    public ResponseEntity<Page<LojaBairroResponse>> listar(
            LojaBairroFiltroDTO filtro,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<LojaBairroResponse> atualizarFrete(
            Long id,
            BigDecimal valorFrete
    ) {
        return ResponseEntity.ok(service.atualizarFrete(id, valorFrete));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}