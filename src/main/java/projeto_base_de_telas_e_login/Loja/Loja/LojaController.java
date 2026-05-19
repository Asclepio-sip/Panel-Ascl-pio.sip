package projeto_base_de_telas_e_login.Loja.Loja;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto_base_de_telas_e_login.Loja.Loja.dto.CreateLojaRequest;
import projeto_base_de_telas_e_login.Loja.Loja.dto.LojaResponse;

import java.util.List;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService service;

    public LojaController(
            LojaService service
    ) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LojaResponse> criar(
            @RequestBody CreateLojaRequest request
    ) {

        return ResponseEntity.ok(
                service.criar(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<LojaResponse>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LojaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody CreateLojaRequest request
    ) {

        return ResponseEntity.ok(
                service.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}