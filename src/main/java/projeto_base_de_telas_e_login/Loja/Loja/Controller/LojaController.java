package projeto_base_de_telas_e_login.Loja.Loja.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.Loja.Loja.Controller.Api.LojaApi;
import projeto_base_de_telas_e_login.Loja.Loja.LojaService;
import projeto_base_de_telas_e_login.Loja.Loja.dto.CreateLojaRequest;
import projeto_base_de_telas_e_login.Loja.Loja.dto.LojaResponse;

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