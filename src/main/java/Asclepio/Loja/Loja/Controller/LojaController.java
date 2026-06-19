package Asclepio.Loja.Loja.Controller;

import Asclepio.Loja.Loja.Controller.Api.LojaApi;
import Asclepio.Loja.Loja.LojaService;
import Asclepio.Loja.Loja.dto.CreateLojaRequest;
import Asclepio.Loja.Loja.dto.LojaFiltroDTO;
import Asclepio.Loja.Loja.dto.LojaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LojaController implements LojaApi {

    private final LojaService service;

    public LojaController(LojaService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<LojaResponse> criar(CreateLojaRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criar(request));
    }

    @Override
    public ResponseEntity<Page<LojaResponse>> listar(LojaFiltroDTO filtro, Pageable pageable) {
        return ResponseEntity.ok(service.listar(filtro, pageable));
    }

    @Override
    public ResponseEntity<LojaResponse> atualizar(Long id, CreateLojaRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}