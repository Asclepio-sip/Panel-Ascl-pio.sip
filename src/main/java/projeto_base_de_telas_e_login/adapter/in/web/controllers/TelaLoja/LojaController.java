package projeto_base_de_telas_e_login.adapter.in.web.controllers.TelaLoja;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Loja.CreateLojaRequest;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.Loja.LojaResponse;
import projeto_base_de_telas_e_login.domain.UseCase.Loja.Loja.LojaUseCase;

import java.util.List;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaUseCase useCase;

    public LojaController(LojaUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<LojaResponse> criar(@RequestBody CreateLojaRequest request) {
        return ResponseEntity.ok(useCase.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<LojaResponse>> listar() {
        return ResponseEntity.ok(useCase.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LojaResponse> atualizar(@PathVariable Long id,
                                                  @RequestBody CreateLojaRequest request) {
        return ResponseEntity.ok(useCase.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        useCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}