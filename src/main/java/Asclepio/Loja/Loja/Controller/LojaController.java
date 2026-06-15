package Asclepio.Loja.Loja.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Asclepio.Loja.Loja.Controller.Api.LojaApi;
import Asclepio.Loja.Loja.LojaService;
import Asclepio.Loja.Loja.dto.CreateLojaRequest;
import Asclepio.Loja.Loja.dto.LojaResponse;

import java.util.List;

@RestController
public class LojaController implements LojaApi {

    private final LojaService service;

    public LojaController(LojaService service) {
        this.service = service;
    }

    public ResponseEntity<LojaResponse> criar(@RequestBody CreateLojaRequest request) {
        return ResponseEntity.ok(service.criar(request));
    }

    public ResponseEntity<List<LojaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    public ResponseEntity<LojaResponse> atualizar(@PathVariable Long id, @RequestBody CreateLojaRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}