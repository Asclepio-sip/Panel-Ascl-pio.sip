package projeto_base_de_telas_e_login.adapter.in.web.controllers.TelaLoja;

import org.springframework.web.bind.annotation.*;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.lojabairros.CreateLojaBairroRequest;
import projeto_base_de_telas_e_login.adapter.in.web.dto.loja.lojabairros.LojaBairroResponse;
import projeto_base_de_telas_e_login.domain.UseCase.Loja.LojaBairro.LojaBairroUseCase;

import java.util.List;

@RestController
@RequestMapping("/loja-bairros")
public class LojaBairroController {

    private final LojaBairroUseCase useCase;

    public LojaBairroController(LojaBairroUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public LojaBairroResponse criar(@RequestBody CreateLojaBairroRequest request) {
        return useCase.criar(request);
    }

    @GetMapping("/loja/{lojaId}")
    public List<LojaBairroResponse> listarPorLoja(@PathVariable Long lojaId) {
        return useCase.listarPorLoja(lojaId);
    }

    @GetMapping
    public List<LojaBairroResponse> listaTodasLojas() {
        return useCase.listarTodos();
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        useCase.deletar(id);
    }
}